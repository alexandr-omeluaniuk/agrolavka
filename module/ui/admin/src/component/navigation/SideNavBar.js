/* 
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import React from 'react';
import { useTranslation } from 'react-i18next';
import NavItem from './NavItem';
import AppURLs from '../../conf/app-urls';
import SideNavBarDesktop from './SideNavBarDesktop';
import SideNavBarMobile from './SideNavBarMobile';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { useTheme } from '@material-ui/core/styles';

export default function SideNavBar(props) {
    const { open, setOpen, onItemSelected, currentModule } = props;
    const { t } = useTranslation();
    const isMobile = useMediaQuery(useTheme().breakpoints.down('sm'));
    // module data
    let moduleItems = [];
    let moduleId = null;
    let modulePath = '';
    if (currentModule) {
        moduleItems = currentModule.getItems();
        moduleId = currentModule.getId();
        modulePath = AppURLs.app + currentModule.path;
    }
    // create sidebar navigation
    const createSideBarNavigation = (items, parentPath, parentId, level) => {
        let navItems = [];
        items.forEach(item => {
            let childs = item.getItems() ? createSideBarNavigation(item.getItems(), parentPath + item.path, parentId + '.' + item.id) : null;
            if (!item.hidden) {
                navItems.push(
                        createNavItem(
                            item.id,
                            parentPath + item.path,
                            item.icon,
                            t(`m_${moduleId}:${parentId}.${item.id}${item.getItems() ? '_group' : ''}`),
                            childs,
                            level
                        )
                );
            }
        });
        level++;
        return navItems;
    };
    
    const createNavItem = (id, path, icon, label, childs, level) => {
        return (
            <NavItem path={path} icon={icon} label={label} onItemSelected={onItemSelected} key={path} itemId={id}>
                {childs}
            </NavItem>
        );
    };
    const navItems = createSideBarNavigation(moduleItems, modulePath, moduleId, 0);
    const isOpen = open && currentModule ? true : false;
    return isMobile ? <SideNavBarMobile open={isOpen} setOpen={setOpen} navItems={navItems} moduleId={moduleId}/> 
                : <SideNavBarDesktop open={isOpen} moduleId={moduleId} navItems={navItems}/>;
}
