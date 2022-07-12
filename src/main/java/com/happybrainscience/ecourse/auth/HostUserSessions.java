package com.happybrainscience.ecourse.auth;

import com.happybrainscience.ecourse.application.ApplicationConfig;
import com.happybrainscience.ecourse.model.ModelClasses;
import com.happybrainscience.ecourse.model.Models;
import com.happybrainscience.ecourse.model.User;
import com.happybrainscience.ecourse.model.UserSession;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletContext;
import net.lilycode.core.hibernate.HibernateSessions;
import net.lilycode.core.hibernate.HibernateSessionsConfiguration;
import net.lilycode.core.hibernate.MySQLInnoDbDialect;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author timothyheider
 */
public class HostUserSessions implements UserSessions {

    private static final Logger LOGGER = Logger.getLogger(HostUserSessions.class);

    public HostUserSessions(ServletContext sc) {
        HibernateSessions mainModel = new HibernateSessions();        
        HibernateSessionsConfiguration hibernateConfig = new HibernateSessionsConfiguration();
        hibernateConfig.setDatabaseHostAddress(ApplicationConfig.DB_HOST.value());
        hibernateConfig.setDatabasePort(ApplicationConfig.DB_PORT.value());
        hibernateConfig.setUpdateSchema(true);
        hibernateConfig.setDatabaseUsername(ApplicationConfig.DB_USERNAME.value());
        hibernateConfig.setDatabasePassword(ApplicationConfig.DB_PASSWORD.value());
        hibernateConfig.setDatabaseDialect(MySQLInnoDbDialect.class);
        hibernateConfig.setAnnotatedClasses(ModelClasses.getClasses());
        hibernateConfig.setDatabaseName(ApplicationConfig.DB_DATABASE.value());
        try {
            mainModel.openSessionManager(hibernateConfig);            
            Models.MAIN = Models.add("main", mainModel);
        } catch (IOException ex) {
            throw new RuntimeException("Hibernate initialization exception", ex);
        }                
    }

    @Override
    public UserSessionContext createNewSession(UserInfo userInfo) {
        try (Session hsession = Models.MAIN.openSession()) {
            Query<User> qry = hsession.getSession().createQuery("from User where emailAddress=:emailAddress");
            qry.setParameter("emailAddress", userInfo.getEmailAddress());
            if (qry.list().isEmpty()) {
                return null;
            } else {
                User user = qry.uniqueResult();
                Transaction tx = hsession.getSession().beginTransaction();
                UserSession userSession = new UserSession();
                userSession.setSessionId(UUID.randomUUID().toString());
                userSession.setSessionUser(user);                
                userSession.setWhenCreated(new Date());
                userSession.setLocale(userInfo.getLocale());
                hsession.getSession().save(userSession);
                tx.commit();
                UserSessionContext context = new UserSessionContext();
                context.setSessionId(userSession.getSessionId());
                context.setWhenCreated(userSession.getWhenCreated());
                context.setUserInfo(userInfo);
                context.setLocale(userInfo.getLocale());
                LOGGER.trace("created new session " + context);
                return context;
            }
        }
    }

    @Override
    public void destroySession(UserSessionContext userSession) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidSession(UserSessionContext userSession) {
        try (Session hsession = Models.MAIN.openSession()) {
            Query<UserSession> qry = hsession.getSession().createQuery("from UserSession where sessionId=:sessionId");
            qry.setParameter("sessionId", userSession.getSessionId());
            if (qry.list().isEmpty()) {
                return false;
            } else {
                Date whenCreated = userSession.getWhenCreated();
                Date now = new Date();
                long t = now.getTime() - whenCreated.getTime();
                t /= 1000; // seconds
                t /= 60; // minutes
                t /= 60; // hours
                t /= 24; // days
                // max 30 days for a valid session
                return (t < 30);
            }
        }
    }

    @Override
    public UserSessionContext getBySessionId(String sessionId) {
        try (Session hsession = Models.MAIN.openSession()) {
            Query<UserSession> qry = hsession.getSession().createQuery("from UserSession where sessionId=:sessionId");
            qry.setParameter("sessionId", sessionId);
            if (qry.list().isEmpty()) {
                return null;
            } else {
                UserSession userSession = qry.uniqueResult();
                if(userSession == null) {
                    return null;
                }
                UserSessionContext sessionContext = new UserSessionContext();
                sessionContext.setSessionId(userSession.getSessionId());
                sessionContext.setWhenCreated(userSession.getWhenCreated());
                UserInfo info = new UserInfo();
                info.setEmailAddress(userSession.getSessionUser().getEmailAddress());
                info.setFullName(userSession.getSessionUser().getFullName());
                info.setLocale(userSession.getLocale());
                sessionContext.setUserInfo(info);
                return sessionContext;
            }
        }
    }

    @Override
    public boolean authenticateUser(UserInfo userInfo, String passwordText) {
        return false;
    }

    @Override
    public UserInfo getUserInfo(String emailAddress) {
        if(emailAddress == null) {
            return null;
        }
        try (Session hsession = Models.MAIN.openSession()) {
            Query<User> qry = hsession.getSession().createQuery("from User where emailAddress=:emailAddress");
            qry.setParameter("emailAddress", emailAddress);
            LOGGER.debug("get user info " + emailAddress);
            if (qry.list().isEmpty()) {
                return null;
            } else {
                User user = qry.uniqueResult();
                UserInfo info = new UserInfo();
                info.setEmailAddress(emailAddress);
                info.setFullName(user.getFullName());
                LOGGER.debug("resolved user " + info);
                return info;
            }
        }
    }

    @Override
    public void stop() {
        Models.MAIN.closeSessionManager();
    }

}
