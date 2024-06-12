
import { Accordion, AccordionDetails, AccordionSummary, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/styles';
import React, { useEffect } from 'react';
import DataService from '../../../service/DataService';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular
    }
}));

function AttributesTree(props) {
    const { name, fieldValue, onChangeFieldValue } = props;
    const classes = useStyles();
    const [attributes, setAttributes] = React.useState(null);

    useEffect(() => {
        if (attributes === null) {
            dataService.get('/agrolavka/protected/product-attributes').then(data => {
                setAttributes(data.data);
                console.log(data.data);
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [attributes]);

    const renderAccordion = () => {
        const list = [];
        attributes.forEach(attr => {
            list.push(
                <Accordion key={attr.id}>
                    <AccordionSummary>
                        <Typography className={classes.heading}>{attr.name} [{attr.items.length}]</Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        TODO
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