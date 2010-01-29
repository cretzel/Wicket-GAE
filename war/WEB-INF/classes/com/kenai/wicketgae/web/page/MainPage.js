
var fadeIns = 0;
var fadeOuts = 0;

function fadeOutFeedback() {
	fadeOuts++;
	if (fadeIns == fadeOuts) {
		$('.feedbackPanelContainer').animate({opacity: 0.3});
	}
}

function fadeOutFeedbackTimeout() {
	setTimeout(fadeOutFeedback, 5000);
}

function fadeInFeedback() {
	var it = $('.feedbackPanelContainer');
	
	fadeIns++;
	if (it.find('li').size() > 0) {
		
		if (it.find('.feedbackPanelERROR').size() > 0) {
			it.find('div').css('backgroundColor', '#ff8700');
		} else {
			it.find('div').css('backgroundColor', '#445566');
		}
		
		if (fadeIns == 1) {
			it.fadeIn(500,  fadeOutFeedbackTimeout);
			it.click(function() {
				it.animate({opacity:0.15});
			});
		} else {
			it.animate({opacity: 0.9},  fadeOutFeedbackTimeout);
		}
		
	} else {
		$('.feedbackPanelContainer').animate({opacity: 0.0});
	}
}

