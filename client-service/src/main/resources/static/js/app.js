$(function () {
    var form = $('#comp-data');
    var codeArea = $('#code-area');
    var classNameField = $('#class-name');

    $(form).submit(function (event) {
        event.preventDefault();

        var srcCode = codeArea.val();
        var className = classNameField.val();
        var formData = JSON.stringify({srcCode: srcCode, className: className});

        var submitResultArea = $('#submit-result');

        $.ajax({
            type: 'POST',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            url: $(form).attr('action'),
            data: formData
        }).done(function (response) {
            processOkResponse(response, submitResultArea)
        }).fail(function (data) {
            processErrorResponse(data, submitResultArea)
        });
    });

    function processOkResponse(response, targetArea) {
        if (response.respStatus === 'success') {
            setSuccess(targetArea);
            $(targetArea).text(response.message);
        } else if (response.respStatus === 'comp_error') {
            setError(targetArea);
            $(targetArea).text(response.message + '\n');
            $(targetArea).append('Compiler message: ' + response.compErrorDetails.compilerMessage + '\n');
            $(targetArea).append('Error code line: ' + response.compErrorDetails.errorColumnNumber + '\n');
            $(targetArea).append('Error position: ' + response.compErrorDetails.errorPosition + '\n');
        } else {
            setError(targetArea);
            $(targetArea).text('Unexpected internal error occurred while processing the submission request!');
        }
    }

    function processErrorResponse(data, targetArea) {
        setError(targetArea);
        if (data.responseText !== '') {
            $(targetArea).text(data.responseText);
        } else {
            $(targetArea).text('Unexpected internal error occurred while processing the submission request!');
        }
    }

    function setSuccess(targetArea) {
        $(targetArea).removeClass('error');
        $(targetArea).addClass('success');
    }

    function setError(targetArea) {
        $(targetArea).removeClass('success');
        $(targetArea).addClass('error');
    }
});