/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import firebase from "firebase/app";
import "firebase/analytics";
import "firebase/messaging";


var messaging = null;

export function initFirebase() {
    // Your web app's Firebase configuration
    // For Firebase JS SDK v7.20.0 and later, measurementId is optional
    var firebaseConfig = {
        apiKey: "AIzaSyCNsi-R0xLTquWz74PdTEUG9f2OtTHvbjk",
        authDomain: "agrolavka-2aecb.firebaseapp.com",
        projectId: "agrolavka-2aecb",
        storageBucket: "agrolavka-2aecb.appspot.com",
        messagingSenderId: "1028755576776",
        appId: "1:1028755576776:web:605e8cfe6046bb58c412fd",
        measurementId: "G-7TE8WCK7XF"
    };
    // Initialize Firebase
    firebase.initializeApp(firebaseConfig);
    firebase.analytics();
    messaging = firebase.messaging();
    console.log('Firebase init completed...');
}

export function deleteFirebaseToken(successCallback) {
    // Delete registration token.
    messaging.getToken().then((currentToken) => {
        console.log('delete the next token: ' + currentToken);
        messaging.deleteToken(currentToken).then(() => {
            console.log('Firebase token deleted.');
        }).catch((err) => {
            console.log('Unable to delete token. ', err);
        });
    }).catch((err) => {
        console.log('Error retrieving registration token. ', err);
    });
}

export function requestFirebaseToken() {
    return new Promise((resolve, reject) => {
        // Get registration token. Initially this makes a network call, once retrieved
        // subsequent calls to getToken will return from cache.
        messaging.getToken({vapidKey: 'BAzvWieDywMujonB48CqlSTDcWmpO7J-eaOg_B3LSkT5rOiTBEj_9MAq0FUupJf2jEx_62mka304PorWUd8nFXk'})
                .then((currentToken) => {
                    if (currentToken) {
                        console.log(currentToken);
                        resolve(currentToken);
                    } else {
                        // Show permission request UI
                        console.log('No registration token available. Request permission to generate one.');
                        resolve(null);
                    }
                }).catch((err) => {
                    console.log('An error occurred while retrieving token. ', err);
                    reject(err);
                });
    });
}
