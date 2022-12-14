// Send the selected files as a multipart file
GLOBAL_FORM_DATA_INPUT_TRANSFORMERS.push((formData) => {
	$('input.form-control[type="file"]').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldName = $elt.name;
			var files = $elt.files;
			if (files.length > 0) {
				if (files.length > 1) {
					$.each(files, (index, file) => {
						var fileName = elt.name;
						var fieldName = fileName + '_' + index;
						formData.append(fieldName, file, fileName);
					});
				} else {
					var file = files[0];
					var fileName = file.name;
					formData.append(fieldName, file, fileName);
				}
			    formData.append("upload_file", true);
			}
			$.each($('#fileUploadPdf_removed_' + fieldName), (index, $hiddenElt) => {
				var isManuallyRemoved = $hiddenElt.value
				formData.append('@fileUpload_removed_' + fieldName, isManuallyRemoved);
			});
		}
	});
});

/** Initialize the input component for selecting and uploading a single PDF file.
 * @param config the map of the configuration elements:
 *      * `name` the name of the field to upload.
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `enableRemove` indicates if the component enables to remove a selected BibTeX file. Default is `true`.
 *      * `basename` the basename of the file that is initially shown in the component.
 *      * `picture` the path to the picture that provides a view on the initially selected file.
 *      * `onSelected` a function invoked when a file is seleted. This function takes the file object as argument.
 */
function initFileUploadSinglePdf(config) {
 	config['fileTypeName'] = 'pdf';
 	config['mimeType'] = 'application/pdf';
 	config['fileExtensionMatcher'] = (vName) => { vName.match(/\.(pdf)$/i) };
 	config['fileExtensionArray'] = [ 'pdf' ];
	(!('componentIdPrefix' in config)) && (config['componentIdPrefix'] = "fileUploadPdf_");
	initFileUploadSingleFile_base(config);
}

/** Initialize the input component for selecting and uploading a single BibTeX file.
 * @param config the map of the configuration elements:
 *      * `name` the name of the field to upload.
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `enableRemove` indicates if the component enables to remove a selected BibTeX file. Default is `true`.
 *      * `onSelected` a function invoked when a file is seleted. This function takes the file object as argument.
 */
function initFileUploadSingleBibTeX(config) {
 	config['fileTypeName'] = 'bibtex';
 	config['mimeType'] = 'text/x-bibtex';
 	config['fileExtensionMatcher'] = (vName) => { vName.match(/\.(bib|bibtex)$/i) };
 	config['fileExtensionArray'] = [ 'bib', 'bibtex' ];
	(!('componentIdPrefix' in config)) && (config['componentIdPrefix'] = "fileUploadBibTeX_");
	initFileUploadSingleFile_base(config);
}

/** Initialize the input component for selecting and uploading a single file.
 * @param config the map of the configuration elements:
 *      * `name` the name of the field to upload.
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `enableRemove` indicates if the component enables to remove a selected BibTeX file. Default is `true`.
 *      * `basename` the basename of the file that is initially shown in the component.
 *      * `picture` the path to the picture that provides a view on the initially selected file.
 *      * `mimeType` the MIME type of the accepted files to upload.
 *      * `fileTypeName`: the internal name of the type of file.
 *      * `fileExtensionMatcher` the file extension of the accepted files to upload.
 *      * `fileExtensionArray` the array of the accepted file extensions.
 *      * `onSelected` a function invoked when a file is seleted. This function takes the file object as argument.
 */
function initFileUploadSingleFile_base(config) {
	(!('removedPrefix' in config)) && (config['removedPrefix'] = "removed_");
	(!('removedComponentIdPrefix' in config)) && (config['removedComponentIdPrefix'] = config['componentIdPrefix'] + config['removedPrefix']);

	var ficonfig = {
		sizeUnits: 'MB',
		showPreview: true,
		showRemove: ('enableRemove' in config ? config['enableRemove'] : true),
		showUpload: false,
		showUploadStats: false,
		showCancel: false,
		showPause: false,
		showClose: false,
		maxFileCount: 1,
		autoReplace: true,
		initialPreviewShowDelete: false,
		dropZoneEnabled: true,
		browseOnZoneClick: true,
		fileTypeSettings: {},
		allowedFileTypes: [ config['fileTypeName'] ],
		allowedFileExtensions: config['fileExtensionArray'],
	};
	ficonfig['fileTypeSettings'][config['fileTypeName']] = (vType, vName) => {
        return (typeof vType !== "undefined") ? vType == config['mimeType'] : config['fileExtensionMatcher'](vName);
    };
	if ('picture' in config && config['picture']) {
		ficonfig['initialPreview'] = [
			"<img src='"+ config['picture'] + "' class='file-preview-image' title='" + config['basename'] + "'>",
		];
	}

	var $component = null;
	var component_r = null;
	if ('obj' in config && config['obj']) {
		$component = config['obj'];
	} else if ('selector' in config && config['selector']) {
		$component = $(config['selector']);
	} else if ('id' in config && config['id']) {
		$component = $('#' + config['id']);
		component_r = config['removedPrefix'] + config['id'];
	} else if ('name' in config && config['name']) {
		$component = $('#' + config['componentIdPrefix'] + config['name']);
		component_r = '#' + config['removedComponentIdPrefix'] + config['name'];
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id, name';
	}
	$component.fileinput(ficonfig);

	if (component_r) {
		$component.on('filecleared', (event) => {
			const $rcmp = $(component_r);
			$.each($rcmp, (index, $elt) => {
				$elt.value = "true";
			});
		});
	}
	if ('onSelected' in config && config['onSelected']) {
		$component.on('fileloaded', (event, file, previewId, fileId, index, reader) => {
			(config['onSelected'])(file);
		});
	}
}
