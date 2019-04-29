$(function () {
    var form = $('#comp-data');
    var solutionCodeArea = $('#solution-code-area');
    var runnerCodeArea = $('#runner-code-area');
    var taskNameIdField = $('#task-name-id');
    var taskNameField = $('#task-name');
    var taskDescriptionField = $('#task-description');
    var taskMethodSignField = $('#task-method-sign');

    $(form).submit(function (event) {
        event.preventDefault();

        var solutionSrcCode = solutionCodeArea.val();
        var runnerSrcCode = runnerCodeArea.val();
        var taskNameId = taskNameIdField.val();
        var taskName = taskNameField.val();
        var taskDescription = taskDescriptionField.val();
        var taskMethodSign = taskMethodSignField.val();

        var formData = JSON.stringify({
            solutionSrcCode: solutionSrcCode,
            runnerSrcCode: runnerSrcCode,
            taskNameId: taskNameId,
            taskName: taskName,
            taskDescription: taskDescription,
            taskMethodSign: taskMethodSign
        });

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
        } else if (response.respStatus === 'bad_request') {
            setError(targetArea);
            $(targetArea).text('Source code cannot be processed! Please, check the validity of the class names in the input fields!');
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