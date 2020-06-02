$(function () {
    $.get({
        url: "header.html",
        success: function (html) {
            $("#header").html(html);
        }
    });

    $.get({
        url: "footer.html",
        success: function (html) {
            $("#footer").html(html);
        }
    });
});
