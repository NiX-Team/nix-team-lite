
 /* jQuery Pre loader
  -----------------------------------------------*/
$(window).load(function(){
    $('.preloader').fadeOut(1000); // set duration in brackets    
});


$(document).ready(function() {

  /* Email
  -----------------------------------------------*/
  var lock = false;
  $('#submit').click(function() {
	  if (lock) return;
	  $.ajax({
		  url: 'servlet/email',
		  type: 'POST',
		  data: {
			  name: $.trim($('#contact-form [name="name"]').val()),
			  email: $.trim($('#contact-form [name="email"]').val()),
			  message: $.trim($('#contact-form [name="message"]').val())
		  },
		  beforeSend: function() {
			  lock = true;
		  },
		  complete: function() {
			  lock = false;
		  },
		  success: function(data) {
			  if ('msg' in data) alert(data.msg);
			  $('#contact-form')[0].reset();
		  },
		  error: function() {
			  alert('发送失败，请稍后重试！');
		  }
	  });
  });

  /* Home Slideshow Vegas
  -----------------------------------------------*/
  $(function() {
    $('body').vegas({
        slides: [
            // { src: 'images/slide-1.jpg' },
            // { src: 'images/slide-2.jpg' }
            { src: 'images/bg-1.png' }
        ],
        timer: false,
        transition: [ 'zoomOut', ]
    });
  });


   /* Back top
  -----------------------------------------------*/
    $(window).scroll(function() {
        if ($(this).scrollTop() > 200) {
        $('.go-top').fadeIn(200);
        } else {
          $('.go-top').fadeOut(200);
        }
        });   
        // Animate the scroll to top
      $('.go-top').click(function(event) {
        event.preventDefault();
      $('html, body').animate({scrollTop: 0}, 300);
      })
      

  /* wow
  -------------------------------*/
  // new WOW({ mobile: false }).init();
  new WOW().init();

  });

