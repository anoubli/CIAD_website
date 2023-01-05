/** Add a click handler that execute an ajax query on a click and after a condition (usually UI query) is validated
 * by the front-end user.
 * @param config the map for configuring the function. The keys of the map are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `url` the URL to pass to Ajax.
 *      * `requestMethod` the type of request, by default 'post'.
 *      * `timeout` the timeout to cinsider after launching the Ajax query. Default value is `100000`.
 *      * `preparePreconditionBox` the function to pass to the SweetAlert box (preConfirm) for preparing the content of the box.`
 *      * `title` the title of the precondition dialog box.`
 *      * `text` the text in the precondition dialog box. If it is not provided, provides `html`.
 *      * `html` the html content in the precondition dialog box. If it is not provided, provides `text`.
 *      * `confirmButtonText` the text of the button of the dialog box to indicate "Yes".
 *      * `confirmButtonColor` the color of the "Yes" button. It is optional.
 *      * `cancelButtonColor` the color of the "Cancel" button. It is optional.
 *      * `prepareData` the function that is invoked for initializing the form data to pass to Ajax. This function takes
 *        the two arguments, the button object, and the form-data object.
 *      * `dataType` the type of data that is expected inthe answer. Default is `null`.
 *      * `failureTitle` the title of the dialog box that indicates the deletion is a failure.
 *      * `failureText` the message to show up when deletion was a failure. The message has two internal arguments:
 *      * `onSuccess` a function that is inoked on success. This function is defined as: `function(button,answer)`, where
 *        `button` is the clicked button, and `answer` is the data replied by the backend. The type of the answer depends
 *         on the given `dataType` above.
 *      * `onFailure` a function that is inoked on failure. This function takes the button as argument.
 */
function attachConditionalHandler_base( config ) {
	config['precondition'] = (event, continuationFunction) => {
		const preconditionOptions = {
   		  title: config['title'],
   		  icon: 'question',
   		  showCancelButton: true,
   		  confirmButtonColor: (config['confirmButtonColor'] ? config['confirmButtonColor'] : '#d33'),
   		  cancelButtonColor: (config['cancelButtonColor'] ? config['cancelButtonColor'] : '#99cc00'),
   		  confirmButtonText: config['confirmButtonText']
   		};
   		if ('html' in config && config['html']) {
			preconditionOptions['html'] = config['html'];
		} else {
			preconditionOptions['text'] = config['text'];
		}
		if ('preparePreconditionBox' in config && config['preparePreconditionBox']) {
			preconditionOptions['preConfirm'] = config['preparePreconditionBox'];
		}
		Swal.fire(preconditionOptions).then(result => {
    		if (result.isConfirmed) {
				const confirmResult = result.value;
				config['precondition-result'] = confirmResult;
				continuationFunction(event);
			}
		});
	};
	attachAjaxHandler_base(config);
}
