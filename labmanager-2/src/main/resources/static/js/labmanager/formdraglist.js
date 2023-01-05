// Send the selected items as an array of keys
GLOBAL_FORM_DATA_INPUT_TRANSFORMERS.push((formData) => {
	$('ul.form-control.list-group').each( (index0, $elt) => {
		if ($($elt).is(':visible') && $elt.id.startsWith('dragList_')) {
			var fieldName = $($elt).attr('name');
			var itemArray = [];
			$($elt).children('li').each((index1, $liElt) => {
				var itemName = $liElt.id;
				itemArray.push(itemName);
			});
			formData.append(fieldName, itemArray);
		}
	});
});

/** Initialize the input component for enabling a drag-drop list of elements.
 * @param config the map of the configuration elements:
 *      * `name` the name of the field to upload. The component id is build from the componentIdPrefix and the given name.
 *      * `groupname` the name of the group that enables to move items between the two lists of items (default is `list-of-items`).
 *      * `componentIdPrefix` the prefix to use for building identifiers from names (default is `dragList_`)
 *      * `componentIdPrefixOff` the prefix to use for building identifiers from names (default is `offDragList_`)
 *      * `sortonitems` indicates if the "on" items are sorted (default is false).
 *      * `sortoffitems` indicates if the "off" items are sorted (default is false).
 *      * `animation` indicates the duration of the UI animation (default is 100).
 */
function initDragList(config) {
	(!('groupname' in config)) && (config['groupname'] = 'list-of-items');
	(!('componentIdPrefix' in config)) && (config['componentIdPrefix'] = "dragList_");
	(!('componentIdPrefixOff' in config)) && (config['componentIdPrefixOff'] = "offDragList_");
	(!('sortonitems' in config)) && (config['sortonitems'] = false);
	(!('sortoffitems' in config)) && (config['sortoffitems'] = false);
	(!('animation' in config)) && (config['animation'] = 100);

	var onHtmlComponent = document.getElementById(config['componentIdPrefix'] + config['name']);
	var offHtmlComponent = document.getElementById(config['componentIdPrefixOff'] + config['name']);

	var soconfig_on = {
		animation: config['animation'],
		group: config['groupname'],
		draggable: '.list-group-item',
		handle: '.list-group-item',
		sort: config['sortonitems'],
		filter: '.sortable-disabled',
		chosenClass: 'active',
	};
	var soconfig_off = {
		group: config['groupname'],
		handle: '.list-group-item',
		sort: config['sortonitems'],
	};
	
	if (!(onHtmlComponent && onHtmlComponent.nodeType && onHtmlComponent.nodeType === 1)) {
		throw `Sortable: \`onHtmlComponent\` must be an HTMLElement, not ${ {}.toString.call(onHtmlComponent) }`;
	}
	if (!(offHtmlComponent && offHtmlComponent.nodeType && offHtmlComponent.nodeType === 1)) {
		throw `Sortable: \`offHtmlComponent\` must be an HTMLElement, not ${ {}.toString.call(offHtmlComponent) }`;
	}

	Sortable.create(onHtmlComponent, soconfig_on);
	Sortable.create(offHtmlComponent, soconfig_off);
}
