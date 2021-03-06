$(function () {
    var form = $('#comp-data');
    var codeArea = $('#code-area');
    var classNameField = $('#class-name');
    var taskNameIdField = $('#task-name-id');

    $(form).submit(function (event) {
        event.preventDefault();

        var srcCode = codeArea.val();
        var className = classNameField.val();
        var taskNameId = taskNameIdField.val();
        var formData = JSON.stringify({srcCode: srcCode, className: className, taskNameId: taskNameId});

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
        var respStatus = response.respStatus;
        if (respStatus === 'success') {
            setSuccess(targetArea);
            $(targetArea).text(response.message);
        } else if (respStatus === 'comp_error') {
            setError(targetArea);
            $(targetArea).text(response.message + '\n');
            $(targetArea).append('Compiler message: ' + response.compErrorDetails.compilerMessage + '\n');
            $(targetArea).append('Error code line: ' + response.compErrorDetails.errorColumnNumber + '\n');
            $(targetArea).append('Error position: ' + response.compErrorDetails.errorPosition + '\n');
        } else if (respStatus === 'bad_request') {
            setError(targetArea);
            $(targetArea).text('Source code cannot be processed! Please, check the validity of the class names in the input fields!');
        } else if(respStatus === 'exec_error' || respStatus === 'not_found') {
            setError(targetArea);
            $(targetArea).text(response.message + '\n');
            $(targetArea).append(response.execMessage);
        } else {
            setError(targetArea);
            $(targetArea).text('Unexpected internal error occurred while processing the submission request!');
        }
    }

    function processErrorResponse(data, targetArea) {
        setError(targetArea);
        var respStatus = data.status;
        if (respStatus === 422) {
            var respObject = JSON.parse(data.responseText);
            $(targetArea).text('Validation failed!\n');
            Object.values(respObject).forEach(function (error) {
                $(targetArea).append('- ' + error + '\n');
            });
        } else if (respStatus === 500) {
            $(targetArea).text('Unexpected internal error occurred while processing the submission request!');
        } else {
            $(targetArea).text(data.responseText);
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