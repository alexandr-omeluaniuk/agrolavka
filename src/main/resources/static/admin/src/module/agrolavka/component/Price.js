/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import Typography from '@material-ui/core/Typography';

function Price(props) {
    const { price } = props;
    const priceFormatted = parseFloat(price).toFixed(2).split('.');
    return (
            <Typography variant={'subtitle1'}>
                {priceFormatted[0]}.
                <Typography component={'span'} variant={'subtitle2'}>{priceFormatted[1]} 
                    <Typography component={'span'} variant={'subtitle2'} color="textSecondary" style={{marginLeft: '5px'}}>BYN</Typography>
                </Typography>
            </Typography>
    );
}

export default Price;

