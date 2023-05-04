/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { useTranslation } from 'react-i18next';
import ResponsiveTab from '../../../component/card/ResponsiveTab';
import UserInterface from './component/UserInterface';

function Settings(props) {
    const { t } = useTranslation();
    const tabNames = [{
            label: t('component.account_menu.t_settings.tabs.interface'),
            icon: 'palette',
            value: 0
    }];
    const tabs = [
        <UserInterface />
    ];
    return (
            <ResponsiveTab tabNames={tabNames} tabs={tabs} />
    );
}

export default Settings;
