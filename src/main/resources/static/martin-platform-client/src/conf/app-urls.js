/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const context = '/admin';

const AppURLs = {
    context: context,
    api: '/api',
    welcome: context + '/welcome',
    app: context + '/app',
    registration: context + '/verification/:validationString'
};

export default AppURLs;
