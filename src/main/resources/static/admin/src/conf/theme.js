/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { createMuiTheme, responsiveFontSizes } from '@material-ui/core/styles';

import red from '@material-ui/core/colors/red';
import pink from '@material-ui/core/colors/pink';
import purple from '@material-ui/core/colors/purple';
import deepPurple from '@material-ui/core/colors/deepPurple';
import indigo from '@material-ui/core/colors/indigo';
import blue from '@material-ui/core/colors/blue';
import lightBlue from '@material-ui/core/colors/lightBlue';
import cyan from '@material-ui/core/colors/cyan';
import teal from '@material-ui/core/colors/teal';
import green from '@material-ui/core/colors/green';
import lightGreen from '@material-ui/core/colors/lightGreen';
import lime from '@material-ui/core/colors/lime';
import yellow from '@material-ui/core/colors/yellow';
import amber from '@material-ui/core/colors/amber';
import orange from '@material-ui/core/colors/orange';
import deepOrange from '@material-ui/core/colors/deepOrange';
import { PRIMARY_COLOR, SECONDARY_COLOR, PRIMARY_CONTRAST, SECONDARY_CONTRAST, THEME } from '../conf/local-storage-keys';

export const drawerWidth = 300;

export const THEME_STANDARD = {
    id: "STANDARD",
    overrideTheme: function (settings) {
        return settings;
    }
};

export const ALL_THEMES = [
    THEME_STANDARD
];

export const COLORS = [
    [ { label: 'Red', color: red }, { label: 'Pink', color: pink }, { label: 'Purple', color: purple }, { label: 'Deep purple', color: deepPurple } ],
    [ { label: 'Indigo', color: indigo }, { label: 'Blue', color: blue }, { label: 'Light blue', color: lightBlue }, { label: 'cyan', color: cyan } ],
    [ { label: 'Teal', color: teal }, { label: 'Green', color: green }, { label: 'Light green', color: lightGreen }, { label: 'Lime', color: lime } ],
    [ { label: 'Yellow', color: yellow }, { label: 'Amber', color: amber }, { label: 'Orange', color: orange }, { label: 'Deep orange', color: deepOrange } ]
];

export function getColorByLabel(label) {
    const allColors = [];
    COLORS.forEach(row => {
        row.forEach(c => {
            allColors.push(c);
        });
    });
    const filtered = allColors.filter(c => c.label === label);
    return filtered.length > 0 ? filtered[0].color : null;
}

export function getPrimaryColor() {
    const saved = localStorage.getItem(PRIMARY_COLOR);
    return saved ? saved : 'Pink';
}

export function getSecondaryColor() {
    const saved = localStorage.getItem(SECONDARY_COLOR);
    return saved ? saved : 'Deep orange';
}

export function getPrimaryColorContrast() {
    const saved = localStorage.getItem(PRIMARY_CONTRAST);
    return saved ? parseInt(saved) : 800;
}

export function getSecondaryColorContrast() {
    const saved = localStorage.getItem(SECONDARY_CONTRAST);
    return saved ? parseInt(saved) : 500;
}

export function savePrimaryColorContrast(contrast) {
    localStorage.setItem(PRIMARY_CONTRAST, contrast);
}

export function saveSecondaryColorContrast(contrast) {
    localStorage.setItem(SECONDARY_CONTRAST, contrast);
}

export function savePrimaryColor(color) {
    localStorage.setItem(PRIMARY_COLOR, color);
}

export function saveSecondaryColor(color) {
    localStorage.setItem(SECONDARY_COLOR, color);
}

export function createTheme(themeId) {
    const primaryColor = getPrimaryColor();
    const secondaryColor = getSecondaryColor();
    const primaryContrast = getPrimaryColorContrast();
    const secondaryContrast = getSecondaryColorContrast();
    if (!themeId) {
        themeId = getDefaultTheme();
    }
    let currentThemeProvider = ALL_THEMES.filter(th => {
        return th.id === themeId;
    })[0];
    let themeSettings = {
        palette: {
            primary: getColorByLabel(primaryColor),
            secondary: getColorByLabel(secondaryColor)
        }
    };
    themeSettings.palette.primary.main = themeSettings.palette.primary[primaryContrast];
    themeSettings.palette.secondary.main = themeSettings.palette.secondary[secondaryContrast];
    themeSettings = currentThemeProvider.overrideTheme(themeSettings);
    let theme = createMuiTheme(themeSettings);
    theme = responsiveFontSizes(theme, {
        breakpoints: ['sm', 'md', 'lg'],
        factor: 2
    });
    //console.log(theme);
    return theme;
};

export function setDefaultTheme(themeId) {
    localStorage.setItem(THEME, themeId);
}

export function getDefaultTheme() {
    return localStorage.getItem(THEME) ? localStorage.getItem(THEME) : THEME_STANDARD.id;
}
