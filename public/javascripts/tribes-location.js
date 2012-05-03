track_position = function() {
    console.log('tracking location...')
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function(position) {
                var latitude = position.coords.latitude
                var longitude = position.coords.longitude

                $.post(routes.update(), {
                    'latitude': latitude,
                    'longitude': longitude
                })

                console.log('location is ' + latitude + ', ' + longitude)
            }, {
                enableHighAccuracy: true, maximumAge: 600000
            }
        )
    } else {
        console.log('geolocation not available!')
    }
}

activate_tab = function(id) {
    clear_tabs()
    $(id).addClass('active')
}

clear_tabs = function() {
    $('#nav-home').removeClass('active')
    $('#nav-vault').removeClass('active')
    $('#nav-armory').removeClass('active')
    $('#nav-jobs').removeClass('active')
    $('#nav-fight').removeClass('active')
    $('#nav-tribe').removeClass('active')
    $('#nav-leaderboard').removeClass('active')
}