$(function () {
    var form = $('#comp-data');
    var formMessages = $('#form-messages');
    var codeArea = $('#code-area');
    var classNameField = $('#class-name');

    $(form).submit(function (event) {
        event.preventDefault();

        var srcCode = codeArea.val();
        var className = classNameField.val();
        var formData = JSON.stringify({code: srcCode, className: className});

        $.ajax({
            type: 'POST',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            url: $(form).attr('action'),
            data: formData
        }).done(function (response) {
            $(formMessages).removeClass('error');
            $(formMessages).addClass('success');
            $(formMessages).text(response.message);
        }).fail(function (data) {
            $(formMessages).removeClass('success');
            $(formMessages).addClass('error');
            if (data.responseText !== '') {
                $(formMessages).text(data.responseText);
            } else {
                $(formMessages).text('An error occurred and the message could not be sent!');
            }
        });
    });
});