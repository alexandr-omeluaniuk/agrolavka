self.addEventListener("push", (event) => {
    event.waitUntil(onPush(event));
});

async function onPush(event) {
    const push = event.data.json();
    console.log("push received", push);
    const {notification = {}, data = {}} = {...push};
    notification.actions = JSON.parse(data['gcm.notification.actions'] ? data['gcm.notification.actions'] : []);
    notification.data = data;
    notification.badge = 'https://agrolavka.by/admin/logo24.png';
    notification.icon = 'https://agrolavka.by/admin/logo64.png';
    await self.registration.showNotification(notification.title, notification);
}

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    console.log(event);
    if (event.notification.actions) {
        clients.openWindow(event.action ? event.action : event.notification.actions[0].action);
    }
});