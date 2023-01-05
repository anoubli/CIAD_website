/** Fill the given FormData object with the values that are associated with the HTML input elements.
 * This function explores the components with the class `form-context` and applies a specific method
 * for each of them for setting up the FormData object.
 * Additionnally, the functions in the global array `GLOBAL_FORM_DATA_INPUT_TRANSFORMERS` are invoked.
 * @param formData the FormData object to fill up.
 */
function fillDataFormInEntityForm(formData) {
	// Send the text fields: {name: value}
	$('input.form-control[type="text"]').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldValue = $elt.value;
			if (fieldValue) {
				var fieldName = $elt.name;
				if (fieldName) {
					formData.append(fieldName, fieldValue);
				}
			}
		}
	});
	// Send selected option in combo: {name: value}
	$('select.form-control-combo').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var $elt1 = $('option:selected', $elt);
			if ($elt1) {
				var fieldValue = $elt1[0].value;
				if (fieldValue) {
					var fieldName = $elt.name;
					if (fieldName) {
						formData.append(fieldName, fieldValue);
					}
				}
			}
		}
	});
	// Send the month fields: {name: 'YYYY-MM'}; or {name: 'YYYY'} if the month is not enabled.
	$('input.form-control[type="month"]').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldValue = $elt.value;
			if (fieldValue) {
				var fieldName = $elt.name;
				if (fieldName) {
					var fieldId = $elt.id;
					var $associatedCheckBox = $('#' + fieldId + '_enableMonth');
					var checkedValue = $associatedCheckBox.prop('checked');
					if (!checkedValue) {
						fieldValue = fieldValue.substring(0, 4);
					}
					formData.append(fieldName, fieldValue);
				}
			}
		}
	});
	// Send the date fields: {name: 'YYYY-MM-DD'}
	$('input.form-control[type="date"]').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldValue = $elt.value;
			if (fieldValue) {
				var fieldName = $elt.name;
				if (fieldName) {
					formData.append(fieldName, fieldValue);
				}
			}
		}
	});
	// Send value of text area: {name: value}
	$('textarea.form-control').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldValue = $elt.value;
			if (fieldValue) {
				var fieldName = $elt.name;
				if (fieldName) {
					formData.append(fieldName, fieldValue);
				}
			}
		}
	});
	// Send the boolean 'positive' value of a checkbox: {name: "true"}
	$('input.form-control[type="checkbox"]').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldName = $elt.name;
			if (fieldName) {
				var fieldValue = $($elt).is(':checked');
				formData.append(fieldName, fieldValue);
			}
		}
	});
	// Send the hidden values: {name: value}
	$('input.form-control[type="hidden"]').each( (index, $elt) => {
		var fieldValue = $elt.value;
		if (fieldValue) {
			var fieldName = $elt.name;
			if (fieldName) {
				formData.append(fieldName, fieldValue);
			}
		}
	});

	$.each(GLOBAL_FORM_DATA_INPUT_TRANSFORMERS, (index, callback) => {
		callback(formData);
	});
}
