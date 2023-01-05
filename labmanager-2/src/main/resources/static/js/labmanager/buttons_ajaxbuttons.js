/** Add a click handler that execute an ajax query on a click and supporting UI feedback.
 * @param config the map for configuring the function. The keys of the map are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `precondition` a function that is invoked before launching the Ajax query. This function takes the
 *        the function for continuing the process as argument. If the pre-condition fails, the continuation function
 *        should not be invoked. If the precondition is a success, the continuation function is supposed to be invoked.
 *        The precondition function take the following arguments: the button, the continuation function.
 *        The continuation function takes no argument.
 *      * `prepareData` the function that is invoked for initializing the form data to pass to Ajax. This function takes
 *        the two arguments, the button object, and the form-data object.
 *      * `url` the URL to pass to Ajax.
 *      * `requestMethod` the type of request, by default 'post'.
 *      * `timeout` the timeout to cinsider after launching the Ajax query. Default value is `100000`.
 *      * `dataType` the type of data that is expected inthe answer. Default is `null`.
 *      * `failureTitle` the title of the dialog box that indicates the deletion is a failure.
 *      * `failureText` the message to show up when deletion was a failure. The message has two internal arguments:
 *      * `onSuccess` a function that is inoked on success. This function is defined as: `function(button,answer)`, where
 *        `button` is the clicked button, and `answer` is the data replied by the backend. The type of the answer depends
 *         on the given `dataType` above.
 *      * `onFailure` a function that is inoked on failure. This function takes button as argument.
 */
function attachAjaxHandler_base( config ) {
	(!('timeout' in config)) && (config['timeout'] = 100000);
	(!('dataType' in config)) && (config['dataType'] = null);
	(!('requestMethod' in config)) && (config['requestMethod'] = 'post');

    var handler = (event) => {
		const $bt = $(event.target);
		const formData = new FormData();
		if ('prepareData' in config && config['prepareData']) {
			config['prepareData']($bt, formData, config['precondition-result']);
		}
		$.ajax({
			url: config['url'],
			timeout: config['timeout'],
			type: config['requestMethod'],
			data: formData,
			dataType: config['dataType'],
			contentType: false,
			processData: false,
			cache: false,
			async:true,
			success: (answer, textStatus, obj) => {
				if ('onSuccess' in config && config['onSuccess']) {
					(config['onSuccess'])($bt, answer);
				}					
			},
			error: (obj, textStatus, errorThrown) => {
				var errCode = obj.statucCode || obj.status;
				var errMsg = '';
				if (obj.responseJSON) {
					errMsg = obj.responseJSON.message ? obj.responseJSON.message : obj.responseJSON.error;
				} else if (obj.responseText) {
					try {
						const json = JSON.parse(obj.responseText);
						if (json) {
							errCode = json.status;
							errMsg = json.message || json.error;
						} else {
							errMsg = obj.responseText;
						}
					} catch (ignored_error) {
						errMsg = obj.responseText;
					}
				} else {
					errMsg = obj.statusText;
				}
				const errMessage = config['failureText'].format(errCode, errMsg);	
				Swal.fire(
						  config['failureTitle'],
						  errMessage,
						  'error'
						).then(result => {
					if ('onFailure' in config && config['onFailure']) {
						(config['onFailure'])($bt);
					}					
				});
			},
		});
    };
    if ('precondition' in config && config['precondition']) {
		const backHandler = handler;
		handler = (event) => {
			(config['precondition'])(event, backHandler);
		}
	}
	if ('obj' in config && config['obj']) {
		const $bt = config['obj'];
		$bt.click(handler);
	} else if ('selector' in config && config['selector']) {
		$(document).on('click', config['selector'], handler);
	} else if ('id' in config && config['id']) {
		$(document).on('click', '#' + config['id'], handler);
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id';
	}
}
