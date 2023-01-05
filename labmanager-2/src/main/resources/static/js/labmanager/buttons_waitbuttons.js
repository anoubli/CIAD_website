/** Change the visual state of the button by adding a spinner
 * @param $bt the button to change.
 * @param text the text in the button.
 */
function setButtonWaitState($bt, text) {
	$bt = $bt.prop("disabled", true);
	var $span = $('<span></span>').attr('class', 'spinner-border spinner-border-sm');
	$span = $span.attr('role', 'status');
	$span = $span.attr('aria-hidden', 'true');
	$bt.empty();
	$bt.append($span);
	$bt.append("&nbsp;");
	$bt.append(text);
}
/** Change the visual state of the button by removing the spinner
 * @param $bt the button to change.
 * @param text the text in the button.
 */
function setButtonStandardState($bt, text) {
	$bt.empty();
	$bt.append(text);
	$bt.prop("disabled", false);
}
/** Add a click handler that execute an ajax query on a click and supporting UI feedback including a spinner in the clicked button.
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
 *      * `text` the text of the button.
 *      * `prepareData` the function that is invoked for initializing the form data to pass to Ajax. This function takes
 *        the form-data object as argument.
 *      * `dataType` the type of data that is expected inthe answer. Default is `null`.
 *      * `failureTitle` the title of the dialog box that indicates the deletion is a failure.
 *      * `failureText` the message to show up when deletion was a failure. The message has two internal arguments:
 *      * `onSuccess` a function that is inoked on success. This function is defined as: `function(button,answer)`, where
 *        `button` is the clicked button, and `answer` is the data replied by the backend. The type of the answer depends
 *         on the given `dataType` above.
 *      * `onFailure` a function that is inoked on failure. This function takes the button as argument.
 */
function attachSpinnerAjaxHandler_base( config ) {
	if ('prepareData' in config && config['prepareData']) {
		const prev = config['prepareData'];
		config['prepareData'] = ($bt, formData, userData) => {
			setButtonWaitState($bt, config['text']);
			prev($bt, formData, userData);
		};
	} else {
		config['prepareData'] = ($bt, formData) => {
			setButtonWaitState($bt, config['text']);
		};
	}
	if ('onSuccess' in config && config['onSuccess']) {
		const prev = config['onSuccess'];
		config['onSuccess'] = ($bt, answer) => {
			prev($bt, answer);
			setButtonStandardState($bt, config['text']);
		};
	} else {
		config['onSuccess'] = ($bt) => {
			setButtonStandardState($bt, config['text']);
		};
	}
	if ('onFailure' in config && config['onFailure']) {
		const prev = config['onFailure'];
		config['onFailure'] = ($bt) => {
			prev();
			setButtonStandardState($bt, config['text']);
		};
	} else {
		config['onFailure'] = ($bt) => {
			setButtonStandardState($bt, config['text']);
		};
	}
	attachAjaxHandler_base(config);
}
