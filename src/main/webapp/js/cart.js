$(document).ready(function() {
  $('.delete-cart-item').click(function(event) {
    event.preventDefault();
    href = $(this).attr('href');
    $.ajax({
      url: href,
      method: 'delete',
    }).done(function(){
      location.reload();
    });
  });
});
