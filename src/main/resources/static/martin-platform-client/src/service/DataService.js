/* 
 * Copyright (C) 2018 Wisent Media
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import AppURLs from '../conf/app-urls';
import { SharedDataService } from './SharedDataService';

class DataService {
    
    constructor() {
        this.abortController = new AbortController();
    }
    
    get = (url) => {
        return this._request('GET', AppURLs.api + url);
    };
    put = (url, data) => {
        return this._request('PUT', AppURLs.api + url, data);
    };
    post = (url, data) => {
        return this._request('POST', AppURLs.api + url, data);
    };
    delete = (url) => {
        return this._request('DELETE', AppURLs.api + url);
    };
    abort = () => {
        this.abortController.abort();
    }
    login(data) {
        return this._request('POST', '/login', data);
    }
    logout() {
        return this._request('POST', '/logout');
    }
    _request = (method, url, payload) => {
        let signal = this.abortController.signal;
        return fetch(url, {
            method: method,
            signal: signal,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: payload ? JSON.stringify(payload) : null
        }).then(function(response) {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                window.location.href = AppURLs.welcome;
            } else {
                response.json().then(errJson => {
                    if (SharedDataService.showNotification) {
                        SharedDataService.showNotification(errJson.message, errJson.details, 'error');
                    }
                });
            }
        }).catch(error => {
            console.error('HTTP error occurred: ' + error);
        });
    };
};

export default DataService;
