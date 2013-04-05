$(document).ready(function() {
  $('.delete-cart-item').click(function(event) {
    event.preventDefault();
    href = $(this).attr('href');
    $.ajax({
      // TODO I know that's ugly - don't blame the messenger, but Command framework
      // in scalatra doesn't recognize params sent from delete request or as a cookie
      // BTW, that looks like a good candidate for patch in framework
      url: href + "?cartId=" + $.cookie("shoppingcart_id"),
      method: 'delete',
      success: function(response) {
        location.reload();
      }
    });
  });
});
