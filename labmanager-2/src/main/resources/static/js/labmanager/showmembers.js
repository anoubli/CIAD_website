/** Initialize the DataTable for showing the members on the front-end.
 * @param config the map for the configuration of the table. Keys are:
 *     * `obj` the jQuery object for the table HTML element.
 *     * `url` the URL to object the publication data through Ajax call.
 *     * `enableFilters` indicates if the filter pane is enabled. Default is true.
 *     * `statusLabels` the map from the member status name to the localized string.
 *     * `types` the map from the general member type to the localized string (sub-key `label`) and the order (sub-key `order`).
 * @return the DataTable object.
 */
function initMemberDataTable(config) {
	(!('enableFilters' in config)) && (config['enableFilters'] = true);

	var dtconfig = {
		ajax: config['url'],
		dom: config['enableFilters'] ? 'Pt' : 't',
		scrollCollapse: true,
		paging: false,
		responsive: true,
		autoWidth: true,
		deferRender: true,
		columns: [
			{
				data: "memberType",
				defaultContent: '',
				render: (data, type, row) => {
					if (data in config['types'] && 'order' in config['types'][data]) {
						return config['types'][data]['order'];
					}
					return '1000';
				},
				type: 'num',
				visible: false,
				searchPanes: {
					show: false,
				},
			},
			{
				data: "person",
				defaultContent: '',
				render: (data, type, row) => {
					var name = data.fullNameWithLastNameFirst;
					if (!name) {
						name = data.fullName;
					}
					if (!name) {
						name = data.lastName + ' ' + data.firstName;
					}
					var url = data.webPageURI;
					var fullLine = '';
					if (url) {
						fullLine = '<a href="' + url + '">' + name + '</a>';
					} else {
						fullLine = name;
					}
					fullLine = '<span class="researchOrganizationMember">' + fullLine + '<span>';
					var title = data.civilTitle
					if (title) {
						fullLine += ', <span class="researchOrganizationMemberCivilTitle">' + title + '</span>';
					}
					return fullLine;
				},
				searchPanes: {
					show: false,
				},
			},
			{
				data: "memberStatus",
				defaultContent: '',
				render: (data, type, row) => {
					var key = data + '_' + row.person.gender;
					if (key in config['statusLabels'] && config['statusLabels'][key]) {
						return config['statusLabels'][key];
					}
					key = data + '_OTHER';
					if (key in config['statusLabels'] && config['statusLabels'][key]) {
						return config['statusLabels'][key];
					}
					return '';
				},
				searchPanes: {
					show: true,
				},
			},
			{
				data: "responsibility",
				defaultContent: '',
				render: (data, type, row) => {
					var key = data + '_' + row.person.gender;
					if (key in config['responsibilityLabels'] && config['responsibilityLabels'][key]) {
						return config['responsibilityLabels'][key];
					}
					key = data + '_OTHER';
					if (key in config['responsibilityLabels'] && config['responsibilityLabels'][key]) {
						return config['responsibilityLabels'][key];
					}
					return '';
				},
				searchPanes: {
					show: true,
				},
			},
			{
				data: "memberType",
				defaultContent: '',
				visible: false,
				searchPanes: {
					show: false,
				},
			},
			{
				data: "otherOrganizations",
				defaultContent: '',
				render: (data, type, row) => {
					if (data) {
						var cntr = row.country;
						return data.map((it) => {
							var orgName = it.acronym;
							if (!orgName) {
								orgName = it.name;
							}
							var nm = orgName;
							if (it.url) {
								nm = "<a href=\"" + it.url + "\">" + orgName + "</a>";
							}
							if (it.countryLabel != cntr) {
								nm += " (" + it.countryLabel + ")";
							}
							return nm;
						}).join(', ');
					}
					return '';
				},
			},
		],
		// Hard coded initial sort of the members
		orderFixed: {
			pre: [ 0, 'asc' ],
		},
		order: [ [1, 'asc'] ],
		rowGroup: {
			startRender: (rows, group) => {
				var label = config['types'][group]['label'];
				if (label) {
					return label;
				}
				return group;
			},
			endRender: null,
			dataSrc: ['memberType'],
		},
	};

	var dtable = config['obj'].DataTable(dtconfig);
	return dtable;
}
