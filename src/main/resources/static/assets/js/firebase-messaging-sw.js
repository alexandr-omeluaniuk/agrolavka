/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here. Other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/8.4.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.4.1/firebase-messaging.js');
//
//// Initialize the Firebase app in the service worker by passing in
//// your app's Firebase config object.
//// https://firebase.google.com/docs/web/setup#config-object
firebase.initializeApp({
    apiKey: "AIzaSyCNsi-R0xLTquWz74PdTEUG9f2OtTHvbjk",
    authDomain: "agrolavka-2aecb.firebaseapp.com",
    projectId: "agrolavka-2aecb",
    storageBucket: "agrolavka-2aecb.appspot.com",
    messagingSenderId: "1028755576776",
    appId: "1:1028755576776:web:605e8cfe6046bb58c412fd",
    measurementId: "G-7TE8WCK7XF"
});
console.log('Firebase init completed...');
// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();

//// Handle incoming messages. Called when:
//// - a message is received while the app has focus
//// - the user clicks on an app notification created by a service worker
////   `messaging.onBackgroundMessage` handler.
//messaging.onMessage((payload) => {
//  console.log('Message received. ', payload);
//  // ...
//});
console.log('on background message registered...');
messaging.onBackgroundMessage((payload) => {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);
  // Customize notification here
  const notificationTitle = 'Background Message Title';
  const notificationOptions = {
    body: 'Background Message body.',
    icon: '/firebase-logo.png'
  };

  self.registration.showNotification(notificationTitle,
    notificationOptions);
});


