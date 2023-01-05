/** Initialize a Bootstrap Datatable for a front-end usage.
 * @param config the configuration of the administration table. Keys are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `dom` It is the specification of the components of the table that are activated.
 *      * `columns` It may be an array of column definitions, excepting the action column if `actionColumn` is true; or it
 *        may be a number of columns. If a number of columns is provided, the array of columns is automatically built up.
 *      * `order` an array that is specifying the order to apply to the values/columns. Default is `[ [1,'asc'] ]`.
 *      * `data` the array of the rows to be displayed.
 *      * `tableParams` the map of values to pass directly to Datatable.
 * @return the table object.
 */
function initFrontTable(config) {
	(!('dom' in config)) && (config['dom'] = 'frip');
	(!('order' in config)) && (config['order'] = [ [1, 'asc'] ]);
	if ('columns' in config && config['columns']) {
		if (!(config['columns'] instanceof Array)) {
			var cols = Array.from({length: config['columns']}, () => {});
			config['columns'] = cols;
		}
	} else {
		throw "You must provide the 'columns' configuration entry";
	}
	
	var $component = null;
	if ('obj' in config && config['obj']) {
		$component = config['obj'];
	} else if ('selector' in config && config['selector']) {
		$component = $(config['selector']);
	} else if ('id' in config && config['id']) {
		$component = $('#' + config['id']);
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id';
	}

	var tableConfig = {
	    dom: config['dom'],
	    renderer: 'bootstrap',
	    scrollCollapse: false,
	    paging: false,
	    responsive: true,
	    autoWidth: true,
	    order: config['order'],
	    columns: config['columns'],
	};
	if ('data' in config) {
		tableConfig['data'] = config['data'];
	}
	if ('tableParams' in config) {
		$.each(config['tableParams'], (key, value) => {
			tableConfig[key] = value;
		});
	}
	var table = $component.DataTable(tableConfig);
	return table;
}
