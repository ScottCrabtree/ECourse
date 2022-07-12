/* global gapi */

console.log('login js started');

var google_sign_in = false; // assume

function do_click_google_signin() {
    google_sign_in = true;
}

function onSuccess(googleUser) {
    if (google_sign_in) {
        console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
        onGoogleSignIn(googleUser);
    } else {
        console.log('disconnect current Google user');
        gapi.auth2.getAuthInstance().disconnect();
    }
}
function onFailure(error) {
    console.log(error);
}
function renderButton() {
    gapi.signin2.render('google-button-signin2', {
        'scope': 'profile email',
        'width': 240,
        'height': 50,
        'longtitle': true,
        'theme': 'light',
        'onsuccess': onSuccess,
        'onfailure': onFailure
    });
}


