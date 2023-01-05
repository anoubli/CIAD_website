/** Add a click handler that execute the deletion process with dialog boxes.
 * @param config the map for configuring the functions. The keys of the map are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `name` the name of the element. If it is provided and `elementNameCallback` is not provided, it is used.  
 *      * `elementNameCallback` a function that is invoked for obtaining the name of the element that is the subject
 *        of the deletion. This function takes zero argument.
 *      * `url` the URL to pass to AJAX for deleting the element. It is used if `urlBuilder` is not provided.
 *      * `urlBuilder` the function for building the URL. It takes no argument and replies the URL. If it is not provided
 *      * `url` is used.
 *      * `questionTitle` title of the dialog box when querying if deleiton must continue.
 *      * `questionText` text to show in the dialog box when querying if deleiton must continue.
 *      * `confirmButtonText` the text of the button of the dialog box to indicate "Yes".
 *      * `confirmButtonColor` the color of the "Yes" button. It is optional.
 *      * `cancelButtonColor` the color of the "Cancel" button. It is optional.
 *      * `successTitle` the title of the dialog box that indicates the deletion is a success.
 *      * `successText` the message to show up when the deletion is a success.
 *      * `failureTitle` the title of the dialog box that indicates the deletion is a failure.
 *      * `failureText` the message to show up when deletion was a failure. The message has two internal arguments:
 *        `{0}` for the name of the element to be deleted, and `{1}` for the error message from the server.
 *      * `onSuccess` a function that is inoked on success. This function takes zero argument. If it is not provided, i.e.,
 *        the key is not in the map, the function will considered to run `location.reload()`.
 */
function attachDeletionHandler_base( config ) {
    var handler = () => {
		var eltname = ('elementNameCallback' in config && config['elementNameCallback']) ? (config['elementNameCallback']()) : config['name'];
	    console.log("**** START DELETION FOR: " + eltname);
    	Swal.fire( {
   		  title: config['questionTitle'],
   		  text: config['questionText'],
   		  icon: 'question',
   		  showCancelButton: true,
   		  confirmButtonColor: (config['confirmButtonColor'] ? config['confirmButtonColor'] : '#d33'),
   		  cancelButtonColor: (config['cancelButtonColor'] ? config['cancelButtonColor'] : '#99cc00'),
   		  confirmButtonText: config['confirmButtonText']
   		} ).then(result => {
			if (result.isConfirmed) {
				const url = config['urlBuilder'] ? config['urlBuilder']() : config['url'];
				fetch(url, {
		    	    method:'DELETE'
		    	} ).then(response => {
		    		if (response.ok) {
						Swal.fire(
						  config['successTitle'],
						  config['successText'],
						  'success'
						).then(result => {
		    				if (result.isConfirmed) {
								if ('onSuccess' in config) {
									if (config['onSuccess']) {
										config['onSuccess']();
									}
								} else {
									location.reload();
								}
		    				}
						});
		    		} else {
		    			var boxmsg = '' + response.status;
		    			if (response.statusText) {
		    				boxmsg = boxmsg + ' - ' + response.statusText;
		    			}
						if ('elementNameCallback' in config && config['elementNameCallback']) {
							eltname = config['elementNameCallback']();
						}
						Swal.fire(
						  config['failureTitle'],
						  config['failureText'].format(eltname, boxmsg),
						  'error'
						);
		    		}
		    	} ).catch(err => {
					if ('elementNameCallback' in config && config['elementNameCallback']) {
						eltname = config['elementNameCallback']();
					}
					Swal.fire(
					  config['failureTitle'],
					  config['failureText'].format(eltname, err),
					  'error'
					);
		    	} );
			}
   		} );
    };
	if ('obj' in config && config['obj']) {
		var $bt = config['obj'];
		$bt.click(handler);
	} else if ('selector' in config && config['selector']) {
		$(document).on('click', config['selector'], handler);
	} else if ('id' in config && config['id']) {
		$(document).on('click', '#TheDeleteButton_' + config['id'], handler);
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id';
	}
}
/** Add a click handler that execute the deletion process without Ajax with dialog boxes.
 * @param config the map for configuring the functions. The keys of the map are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `name` the name of the element. If it is provided and `elementNameCallback` is not provided, it is used.  
 *      * `elementNameCallback` a function that is invoked for obtaining the name of the element that is the subject
 *        of the deletion. This function takes zero argument.
 *      * `onDeletion` the function for doing the deletion.
 *      * `questionTitle` title of the dialog box when querying if deleiton must continue.
 *      * `questionText` text to show in the dialog box when querying if deleiton must continue.
 *      * `confirmButtonText` the text of the button of the dialog box to indicate "Yes".
 *      * `confirmButtonColor` the color of the "Yes" button. It is optional.
 *      * `cancelButtonColor` the color of the "Cancel" button. It is optional.
 *      * `successTitle` the title of the dialog box that indicates the deletion is a success.
 *      * `successText` the message to show up when the deletion is a success.
 *      * `failureTitle` the title of the dialog box that indicates the deletion is a failure.
 *      * `failureText` the message to show up when deletion was a failure. The message has two internal arguments:
 *        `{0}` for the name of the element to be deleted, and `{1}` for the error message from the server.
 *      * `onSuccess` a function that is inoked on success. This function takes zero argument. If it is not provided, i.e.,
 *        the key is not in the map, the function will considered to run `location.reload()`.
 */
function attachDeletionHandlerNoAjax_base( config ) {
    var handler = () => {
		var eltname = ('elementNameCallback' in config && config['elementNameCallback']) ? (config['elementNameCallback']()) : config['name'];
	    console.log("**** START DELETION FOR: " + eltname);
    	Swal.fire( {
   		  title: config['questionTitle'],
   		  text: config['questionText'],
   		  icon: 'question',
   		  showCancelButton: true,
   		  confirmButtonColor: (config['confirmButtonColor'] ? config['confirmButtonColor'] : '#d33'),
   		  cancelButtonColor: (config['cancelButtonColor'] ? config['cancelButtonColor'] : '#99cc00'),
   		  confirmButtonText: config['confirmButtonText']
   		} ).then(result => {
			if (result.isConfirmed) {
				try {
					if ('onDeletion' in config && config['onDeletion']) {
						config['onDeletion']();
					}
					Swal.fire(
					  config['successTitle'],
					  config['successText'],
					  'success'
					).then(result => {
	    				if (result.isConfirmed) {
							if ('onSuccess' in config) {
								if (config['onSuccess']) {
									config['onSuccess']();
								}
							}
	    				}
					});
	    		} catch (exception) {
	    			var boxmsg = '' + exception;
					if ('elementNameCallback' in config && config['elementNameCallback']) {
						eltname = config['elementNameCallback']();
					}
					Swal.fire(
					  config['failureTitle'],
					  config['failureText'].format(eltname, boxmsg),
					  'error'
					);
	    		}
			}
   		} );
    };
	if ('obj' in config && config['obj']) {
		var $bt = config['obj'];
		$bt.click(handler);
	} else if ('selector' in config && config['selector']) {
		$(document).on('click', config['selector'], handler);
	} else if ('id' in config && config['id']) {
		$(document).on('click', '#TheDeleteButton_' + config['id'], handler);
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id';
	}
}
