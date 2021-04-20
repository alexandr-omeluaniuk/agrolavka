/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { useTranslation } from 'react-i18next';
import Dropdown from '../../../../component/form/input/Dropdown';

function LanguageDropdown (props) {
    const { t, i18n } = useTranslation();
    const [selectedLanguage, setSelectedLanguage] = React.useState(i18n.language);
    let languages = i18n.options.whitelist.filter(l => { return l !== 'cimode'; });
    const languageOptions = [];
    languages.forEach(lang => {
        languageOptions.push({
            label: t('language.' + lang),
            value: lang
        });
    });
    const changeLang = (lang) => {
        setSelectedLanguage(lang);
        i18n.changeLanguage(lang);
    };
    
    return (
            <Dropdown variant="outlined" label={t('component.account_menu.t_settings.language')}
                options={languageOptions} value={selectedLanguage}
                fullWidth={true} onChange={(e) => changeLang(e.target.value)}/>
    );
}

export default LanguageDropdown;
