
import { Accordion, AccordionDetails, AccordionSummary, Checkbox, FormControlLabel, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/styles';
import React, { useEffect } from 'react';
import DataService from '../../../service/DataService';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    items: {
        display: 'flex',
        flexDirection: 'column',
        width: '100%'
    },
    heading: {
        display: 'flex',
        justifyContent: 'space-between',
        width: '100%'
    },
    headingCol: {
        flex: 1
    }
}));

function AttributesTree(props) {
    const { name, fieldValue, onChangeFieldValue } = props;
    const classes = useStyles();
    const [attributes, setAttributes] = React.useState(null);

    useEffect(() => {
        if (attributes === null) {
            dataService.get('/agrolavka/protected/product-attributes?order_by=name&order=asc').then(data => {
                setAttributes(data.data);
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [attributes]);

    const renderAccordion = () => {
        const list = [];
        attributes.forEach(attr => {
            const selectedCount = attr.items.filter(x => fieldValue.includes(x.id)).length;
            const checkboxes = [];
            const attrItems = attr.items;
            attrItems.sort((a, b) => {
                if (a.name < b.name) {
                    return -1;
                }
                if (a.name > b.name) {
                    return 1;
                }
                return 0;
            });
            attrItems.forEach(item => {
                checkboxes.push(
                    <FormControlLabel key={item.id} label={item.name} variant={'standard'} control={(
                        <Checkbox checked={fieldValue.includes(item.id)} onChange={(e) => {
                            let fieldValueCopy = JSON.parse(JSON.stringify(fieldValue));
                            if (e.target.checked) {
                                fieldValueCopy.push(item.id)
                            } else {
                                fieldValueCopy = fieldValueCopy.filter(i => i !== item.id);
                            }
                            onChangeFieldValue(name, fieldValueCopy);
                        }} name={item.name} color="secondary"/>
                    )}/>
                );
            });
            list.push(
                <Accordion key={attr.id}>
                    <AccordionSummary className={classes.heading}>
                        <div className={classes.headingCol}><Typography variant={'caption'}><b>{attr.name}</b></Typography></div>
                        <div><Typography variant={'caption'}>{selectedCount} / {attr.items.length}</Typography></div>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div className={classes.items}>
                        {checkboxes}
                        </div>
                    </AccordionDetails>
                </Accordion>
            );
        });
        return list;
    };

    if (!attributes) {
        return null;
    }

    return (
        <div>{renderAccordion()}</div>
    );
}

export default AttributesTree;