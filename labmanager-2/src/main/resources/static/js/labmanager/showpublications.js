/** Function that build the default content for the publication details.
 * @param d the data object.
 * @param config the Datatable configuration.
 * @return the HTML code for the publication details.
 */
function formatPublicationDetails(d, config) {
	var labels = config['detailledInfoLabels'];
	var abstractText = d.abstractText && d.abstractText != "" ? ("<tr><td>"
		+ labels['abstract'] + "</td><td>" + d.abstractText + '</td></tr>') : "";
	var note = d.note && d.note != "" ? ("<tr><td>" + labels['note']
		+ "</td><td>" + d.note + '</td></tr>') : "";
	var keywords = d.keywords && d.keywords != "" ? ("<tr><td>"
		+ labels['keywords'] + "</td><td>" + d.keywords + '</td></tr>') : "";
	var scimagoQuartile = d.scimagoQIndex && d.scimagoQIndex != "" && d.scimagoQIndex != "NR" ? ("<tr><td>"
		+ labels['scimago'] + "<td>" + d.scimagoQIndex
		+ "&nbsp;[<span title=\"" + labels['scimagoNote'] + "\" style=\"cursor:help\">*</span>]"
		+ (d.scimagoQIndex_imageUrl && d.scimagoQIndex_imageUrl != "" ? '<img class="publicationDetailsScimagoQuartile" src="'
		+ d.scimagoQIndex_imageUrl + '" />' : '') + '</td></tr>') : "";
	var wosQuartile = d.wosQIndex && d.wosQIndex != "" && d.wosQIndex != "NR" ? ("<tr><td>"
		+ labels['wos'] + "</td><td>" + d.wosQIndex
		+ "&nbsp;[<span title=\"" + labels['wosNote'] + "\" style=\"cursor:help\">*</span>]"
		+ '</td></tr>') : "";
	var coreRanking = d.coreRanking && d.coreRanking != "" ? ("<tr><td>"
		+ labels['core'] + "</td><td>" + d.coreRanking
		+ "&nbsp;[<span title=\"" + labels['coreNote'] + "\" style=\"cursor:help\">*</span>]"
		+ '</td></tr>') : "";
	var impactFactor = d.impactFactor && d.impactFactor != "" ? ("<tr><td>"
		+ labels['impactFactor'] + "</td><td>" + d.impactFactor
		+ "&nbsp;[<span title=\"" + labels['impactFactorNote'] + "\" style=\"cursor:help\">*</span>]"
		+ '</td></tr>') : "";
	var links = d.htmlLinks && d.htmlLinks != "" ? ("<tr><td>"
		+ labels['links'] + "</td><td>" + d.htmlLinks +'</td></tr>') : "";
	var downloads = '';
	if (d.htmlDownloads && d.htmlDownloads.length > 0) {
		downloads = "<tr><td>" + labels['downloads'] + "</td><td>";
		d.htmlDownloads.forEach((elt) => {
			downloads += elt + ' ';
		});
		downloads += '</td></tr>';	
	};
	var exports = '';
	if (d.htmlExports && d.htmlExports.length > 0) {
		exports = "<tr><td>" + labels['exports'] + "</td><td>";
		d.htmlExports.forEach((elt) => {
			exports += elt + ' ';
		});
		exports += '</td></tr>';	
	}
	var management = '';
	if (sessionStorage.getItem('status') != null && ((d.htmlEdit && d.htmlEdit != "") || (d.htmlDelete && d.htmlDelete != ""))) {
		management = "<tr><td>" + labels['management'] + "</td><td>" + d.htmlEdit + d.htmlDelete + '</td></tr>';
	}
	return '<div class="publicationDetails"><div class="table-responsive"><table class="table">'
		+ "<tr><td>" + labels['authors'] + "</td><td class=\"publicationAuthors\">" + d.htmlAuthors + '</td></tr>'
		+ "<tr><td>" + labels['type'] + "</td><td>" + d.htmlCategoryLabel + ' (' + d.category + ') / '
		+ d.htmlTypeLabel + ' (' + d.type + ')</td></tr>'
		+ links
		+ abstractText
		+ note
		+ keywords
		+ scimagoQuartile
		+ wosQuartile
		+ coreRanking
		+ impactFactor
		+ downloads
		+ exports
		+ management
		+ '</table></div></div>';
}

/** Initialize the DataTable for showing the publications on the front-end.
 * @param config the map for the configuration of the table. Keys are:
 *     * `obj` the jQuery object for the table HTML element.
 *     * `url` the URL to object the publication data through Ajax call.
 *     * `categoryLabels` the map that provides the labels of the publication categories per category name.
 *     * `groupLabelPlurial` the text of a group of rows, plurial version. This text could contain `{0}` for
 *       for being replaced by the category label, and `{1}` for being replaced by the number of elements
 *       in the group (always greater than `1`).
 *     * `groupLabelSingular` the text of a group of rows, singular version. This text could contain `{0}` for
 *       for being replaced by the category label, and `{1}` for being replaced by the number of elements
 *       in the group (always `0` or `1`).
 *     * `infoText` the text to put in the information box of the table. Inside, `{0}` is replaced by the
 *       number of publications in total in the table.
 *     * `infoTextId` the identifier of the HTML element that should contains the table information (see `infoText`).
 *       Default is `tableInfos`.
 *     * `editionUrl` the URL for editing the publication. The URL will be completed with "=<PUBLICATION_ID>".
 *     * `publicationDescription` a function that generate the HTML code for showing a publication in the main part of the table.
 *       If it is not provided, a default rendering is used. Three arguments are provided to this function: the
 *       data itself, the type, the row data provided by the backend.
 *     * `publicationDetails` a function that generates the HTML code for showing the publications details.
 *       If it is not provided the function `getPublicationDatatableDetails` is invoked. The function takes the data
 *       map and the DataTable configuration as arguments.
 *     * `enableDetails` indicates if the publications details could be shown/hidden interactively. Default is true.
 *     * `enableFilters` indicates if the filter pane is enabled. Default is true.
 *     * `searchPanes` the array of additional search panes. They are displayed if `enableFilters` is true.
 *     * `enableAuthorFilter` indicates if the filter pane dedicated to author names is enabled. Default is true.
 * @return the DataTable object.
 */
function initPublicationDataTable(config) {
	(!('enableFilters' in config)) && (config['enableFilters'] = true);
	(!('enableYearFilter' in config)) && (config['enableYearFilter'] = true);
	(!('enableTypeFilter' in config)) && (config['enableTypeFilter'] = true);
	(!('enableAuthorFilter' in config)) && (config['enableAuthorFilter'] = true);
	(!('infoTextId' in config)) && (config['infoTextId'] = 'tableInfos');
	(!('publicationDescription' in config)) && (config['publicationDescription'] = (data, type, row) => {
		var cnt = '<p class="publicationTitlePart"><span class="publicationTitle">' + row.title + '</span>';
		if (config['editionUrl']) {
		    cnt += "<a href=\"";
		    cnt += config['editionUrl'] + row.id;
		    cnt += "\" class=\"noLink\"><span class=\"fa-solid fa-pen\"></span></a>";
		}
		cnt += '</p><p class="publicationAuthors">'
			+ row.htmlAuthors + '</p><p class="publicationDetails">'
			+ row.htmlPublicationDetails + '. ' + row.publicationYear + '.</p>';
		return cnt;
	});
	(!('publicationDetails' in config)) && (config['publicationDetails'] = formatPublicationDetails);
	(!('enableDetails' in config)) && (config['enableDetails'] = true);

	var dtconfig = {
		"ajax": config['url'],
		dom: config['enableFilters'] ? 'Ptp' : 'tp',
		scrollCollapse: true,
		paging: true,
		responsive: true,
		autoWidth: true,
		deferRender:true,
		columnDefs: [
			{ type: 'rank', targets: 4 }
		],
		"columns": [
			{
				className: 'details-control',
				orderable: false,
				data: null,
				defaultContent: '',
			},
			{
				data: "title",
				searchPanes: {
					show: false,
				},
				render: config['publicationDescription'],
			},
			{
				data: "authors",
				searchPanes: {
					show: false,
				},
			},
			{
				data: "year",
				searchPanes: {
					show: config['enableYearFilter'],
				},
			},
			{
				data: "category",
				render: ( data, type, row ) => {
					return config['categoryLabels'][data];
				},
				searchPanes: {
					show: config['enableTypeFilter'],
				},
			},
			{
				data: "htmlDownloads",
				searchPanes: {
					show: false,
				},
			},
		],
		// Hard coded initial sort of the publication
		order: [ [4, 'asc'], [3, 'desc'], [2, 'asc'] ],
		rowGroup: {
			startRender: (rows, group) => {
				var label = config['categoryLabels'][group];
				if (label) {
					var count = rows.count();
					var text = (count > 1) ? config['groupLabelPlurial'] : config['groupLabelSingular'];
					return text.format(label, count);
				}
				return group;
			},
			endRender: null,
			dataSrc: ['category', 'year'],
		},
	};

	if (config['enableFilters']) {
		var n = 0;
		if (config['enableYearFilter']) {
			n++;
		}
		if (config['enableTypeFilter']) {
			n++;
		}
		if (config['enableAuthorFilter']) {
			n++;
		}
		if (n > 0) {
			dtconfig['searchPanes'] = {
				panes: config['searchPanes'],
				layout: 'columns-' + n,
		     };
		}
	}

	if ('infoText' in config && config['infoText']) {
		dtconfig['initComplete'] = (settings, json) => {
			$('#' + config['infoTextId']).html(config['infoText'].format(settings.fnRecordsTotal()));
		};
	}

	var dtable = config['obj'].DataTable(dtconfig);
	// Toggle the visibility
	dtable.column(2).visible(false);
	dtable.column(3).visible(false);
	dtable.column(4).visible(false);
	dtable.column(5).visible(false);

	if (config['enableDetails'] && 'publicationDetails' in config && config['publicationDetails']) {
		$(document).on('click', "tbody td.details-control", (event) => {
			var td = $(event.target);
			__doOpenOrClosePublicationDetails(config, dtable, td, td, null);
		});
		$(document).on('click', "tbody td span.publicationTitle", (event) => {
			var td = $(event.target);
			__doOpenOrClosePublicationDetails(config, dtable, td, null, td);
		});
	}

	return dtable;
}

function __doOpenOrClosePublicationDetails(config, dtable, td, icontd, titletd) {
		var tr = td.closest('tr');
		var row = dtable.row(tr);
		if (!icontd) {
			icontd = tr.find("td.details-control");
		}
		if (!titletd) {
			titletd = tr.find("td span.publicationTitle");
		}
		if (row.child.isShown()) {
			icontd.removeClass('opened');
			titletd.removeClass('opened');
			tr.removeClass('shown');
			row.child('').hide();
		} else {
			var details = config['publicationDetails'](row.data(), config);
			row.child(details).show();
			icontd.addClass('opened');
			titletd.addClass('opened');
			tr.addClass('shown');
 		}	
}

function __getComponentAndSelection(config) {
	var $component = null;
	var componentSelector = null;
	if ('obj' in config && config['obj']) {
		componentSelector = "#" + config['obj'].id;
		$component = config['obj'];
	} else if ('selector' in config && config['selector']) {
		componentSelector = config['selector'];
		$component = $(componentSelector);
	} else if ('id' in config && config['id']) {
		componentSelector = '#' + config['id'];
		$component = $(componentSelector);
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id';
	}
	return {'component': $component, 'selector': componentSelector}; 
}

/** Attach a click handler for downloading a global document.
 * @param config the configuration of the functions. Keys are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `dataSource` the JQuery object for the DataTable that contains the shown data.
 *      * `url` the URL that enables to download the document
 */
function attachGlobalPublicationDownloadHandler(config) {
	var {component: $component, selector: componentSelector} = __getComponentAndSelection(config);
	if ((!('dataSource' in config)) || (!config['dataSource'])) {
		throw 'You must specify the following parameter: table';
	}
	$(document).on('click', componentSelector, () => {
		const typesel = $(config['perTypeSelector']).is(':checked');
		const yearsel = $(config['perYearSelector']).is(':checked');
		const source = config['dataSource'];
		var list = "";
		source.rows( { filter : 'applied'} ).data().each((r) => {
			list += r.id + ",";
		});
		var hostURL = new URL(window.location.href);
		var u = new URL(config['url'], hostURL.origin);
		u.searchParams.append('id', list);
		u.searchParams.append('inAttachment', true);
		u.searchParams.append('groupByCategory', typesel);
		u.searchParams.append('groupByYear', yearsel);
		console.log("*** START DOWNLOADING: " + u.href);
		location.href = u.href;
	});
}

/** Attach a change handler for changing the search criteria in the publication table.
 * @param config the configuration of the functions. Keys are:
 *     * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *       provided, the `selector` or `obj` must be provided.
 *     * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *       provided, the `id` or `obj` must be provided.
 *     * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *       provided, the `selector` or `id` must be provided.
 *     * `table` the JQuery object for the DataTable.
 *     * `infoText` the text to put in the information box of the table. Inside, `{0}` is replaced by the
 *       total number of records in the table.
 *     * `infoTextPartial` the text to put in the information box of the table. Inside, `{0}` is replaced by the
 *       number of publications in number of elements elements in the table, and {1} is replaced by the
 *       total number of records in the table.
 *     * `infoTextId` the selector of the HTML component that contains information. Default is `#tableInfos` 
 */
function attachSearchCriteriaHandler(config) {
	(!('infoTextId' in config)) && (config['infoTextId'] = 'tableInfos');
	var {selector: componentSelector} = __getComponentAndSelection(config);
	$(document).on('keyup', componentSelector, (event) => {
		const sval = $(event.target).val();
		config['table'].search(sval).draw();
		const info = config['table'].page.info();
		const totalRecords = info.recordsTotal;
		if(sval.length > 0) {
			const numberRecords = info.recordsDisplay;
			$('#' + config['infoTextId']).html(config['infoTextPartial'].format(numberRecords, totalRecords));
		} else {
			$('#' + config['infoTextId']).html(config['infoText'].format(totalRecords));
		}
	});
}

function __getCategoryRank(config, category) {
	var categorySub = category;
	var indexOfAccr = categorySub.indexOf("(");
	if(indexOfAccr >= 0) {
		categorySub = categorySub.substring(0, indexOfAccr - 1);
	}
	var ordinal = 1000;
	if ('categoryRanks' in config && categorySub in config['categoryRanks']) {
		ordinal = config['categoryRanks'][categorySub] || 0;
	}
	return ordinal;
}

/** Initialize the DataTable sort feature.
 * @param config the configuration of the functions. Keys are:
 *     * `categoryRanks` the map from the category labels to the category ranks.
 */
function initSortCriteriaFeature(config) {
	// Create functions in DataTable for sorting by category rank
	$.fn.dataTableExt.oSort["rank-asc"] = (x, y) => {
		var categoryRankX = __getCategoryRank(config, x);
		var categoryRankY = __getCategoryRank(config, y);
		if( categoryRankX > categoryRankY ) {
			return 1;
		} else if (categoryRankX < categoryRankY) {
			return -1;
		}
		return 0;
	};
	$.fn.dataTableExt.oSort["rank-desc"] = function (x, y) {
		var categoryRankX = __getCategoryRank(config, x);
		var categoryRankY = __getCategoryRank(config, y);
		if( categoryRankX > categoryRankY ) {
			return -1;
		} else if (categoryRankX < categoryRankY) {
			return 1;
		}
		return 0;
	};
}

/** Attach a change handler for changing the sorting criteria in the publication table.
 * @param config the configuration of the functions. Keys are:
 *     * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *       provided, the `selector` or `obj` must be provided.
 *     * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *       provided, the `id` or `obj` must be provided.
 *     * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *       provided, the `selector` or `id` must be provided.
 *     * `table` the JQuery object for the DataTable.
 *     * `perNameSelector` the selector of the HTML component (radio button) that indicates the
 *       sorting is per author name.
 *     * `perTypeSelector` the selector of the HTML component (radio button) that indicates the
 *       sorting is per publication type.
 *     * `perYearSelector` the selector of the HTML component (radio button) that indicates the
 *       sorting is per publication year.
 */
function attachSortingCriteriaHandler(config) {
	var {selector: componentSelector} = __getComponentAndSelection(config);
	const table = config['table'];
	$(document).on('click', componentSelector, (event) => {
		if ($(config['perNameSelector']).is(':checked')) {
			table.column( '2' ).order( 'asc' );
		} else if ($(config['perTypeSelector']).is(':checked')) {
			table.column( '4' ).order( 'asc' );
		} else if ($(config['perYearSelector']).is(':checked')) {
			table.column( '3' ).order( 'desc' );
		} else {
			// Default: per type, year, name
			table.order([ [4, 'asc'], [3, 'desc'], [2, 'asc'] ]);
		}
		table.draw();
	});
}

/** Attach a change handler for changing the grouping criteria in the publication table.
 * @param config the configuration of the functions. Keys are:
 *     * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *       provided, the `selector` or `obj` must be provided.
 *     * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *       provided, the `id` or `obj` must be provided.
 *     * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *       provided, the `selector` or `id` must be provided.
 *     * `table` the JQuery object for the DataTable.
 *     * `perTypeSelector` the selector of the HTML component (checkbox) that indicates if the
 *       grouping per publication type is enabled.
 *     * `perYearSelector` the selector of the HTML component (checkbox) that indicates the
 *       grouping per publication year is enabled.
 */
function attachGroupingCriteriaHandler(config) {
	var {selector: componentSelector} = __getComponentAndSelection(config);
	const table = config['table'];
	$(document).on('click', componentSelector, (event) => {
		const typesel = $(config['perTypeSelector']).is(':checked');
		const yearsel = $(config['perYearSelector']).is(':checked');
   		if(yearsel && typesel) {
			// Group by year & category
			table.rowGroup().enable();
			table.rowGroup().dataSrc(['category', 'year']);
		} else if (typesel) {
			// Group by type
			table.rowGroup().enable();
			table.rowGroup().dataSrc( 'category');
		} else if (yearsel) {
			// Group by year
			table.rowGroup().enable();
			table.rowGroup().dataSrc( 'year');
		} else {
			table.rowGroup().disable();
			table.rowGroup().dataSrc();
		}
		table.draw();
	});
}
