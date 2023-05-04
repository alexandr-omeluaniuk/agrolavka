/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
import React, { useRef } from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import Paper from '@material-ui/core/Paper';
import FormHelperText from '@material-ui/core/FormHelperText';
import { makeStyles } from '@material-ui/core/styles';
import { Editor } from '@tinymce/tinymce-react';

const useStyles = makeStyles(theme => ({
    label: {
        position: 'relative',
        transform: 'none',
        padding: theme.spacing(2)
    },
    control: {
        width: '100%'
    },
    root: {
        border: 'none'
    }
}));

function HTMLEditor (props) {
    const { label, value, name, onChange, helperText, align, variant,  ...other } = props;
    const classes = useStyles();
    const editorRef = useRef(null);

    const onBlur = () => {
        onChange(name, editorRef.current.getContent());
    };
    
    return (
        <Paper variant="outlined" className={classes.root}>
            <FormControl {...other} error={helperText ? true : false} className={classes.control}>
                <InputLabel className={classes.label}>{label}</InputLabel>
                <Editor
                    apiKey="2r7vkdqi7ezyg30joze388rj9igxihwgz4xwwbyl110rx7jb"
                    onInit={(evt, editor) => editorRef.current = editor}
                    initialValue={value}
                    init={{
                        height: 500,
                        menubar: true,
                        plugins: [
                            'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview', 'anchor',
                            'searchreplace', 'visualblocks', 'code', 'fullscreen',
                            'insertdatetime', 'media', 'table', 'code', 'help', 'wordcount'
                        ],
                        toolbar: 'undo redo | formatselect | ' +
                            'bold italic backcolor | alignleft aligncenter ' +
                            'alignright alignjustify | bullist numlist outdent indent | ' +
                            'removeformat | help | table tabledelete | tableprops tablerowprops tablecellprops | ' + 
                            'tableinsertrowbefore tableinsertrowafter tabledeleterow | ' + 
                            'tableinsertcolbefore tableinsertcolafter tabledeletecol',
                        language: 'ru',
                        content_style: ''
                    }}
                    onBlur={onBlur}
                />
                <FormHelperText error={helperText ? true : false}>{helperText ? helperText : ''}</FormHelperText>
            </FormControl>
        </Paper>
    );
}

export default HTMLEditor;

