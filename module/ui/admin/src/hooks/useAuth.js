/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const useAuth = () => useContext(AuthContext);

export default useAuth;


