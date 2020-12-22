@extends('custom_layout.welcomePage.welcomePage_app')

@section('content')

    <script type="text/javascript">

        function initAutocomplete() {
            var trnava = {lat: 48.3767994, lng: 17.5835082};

            var map = new google.maps.Map(document.getElementById('map'), {
                center: trnava,
                zoom: 11,
                mapTypeId: 'roadmap'
            });

                @foreach($problems as $problem)
            var loc = split(" {{ $problem->poloha }}");
            addMarker(getLocVar(loc[0], loc[1]), map);
            @endforeach

            // Create the search box and link it to the UI element.
            var input = document.getElementById('pac-input');
            var searchBox = new google.maps.places.SearchBox(input);
            map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

            // Bias the SearchBox results towards current map's viewport.
            map.addListener('bounds_changed', function () {
                searchBox.setBounds(map.getBounds());
            });

            var markers = [];
            // Listen for the event fired when the user selects a prediction and retrieve
            // more details for that place.
            searchBox.addListener('places_changed', function () {
                var places = searchBox.getPlaces();

                if (places.length == 0) {
                    return;
                }

                // Clear out the old markers.
                markers.forEach(function (marker) {
                    marker.setMap(null);
                });
                markers = [];

                // For each place, get the icon, name and location.
                var bounds = new google.maps.LatLngBounds();
                places.forEach(function (place) {
                    if (!place.geometry) {
                        console.log("Returned place contains no geometry");
                        return;
                    }

                    // Create a marker for each place.
                    markers.push(new google.maps.Marker({
                        map: map,
                        title: place.name,
                        position: place.geometry.location,
                        icon: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
                    }));

                    if (place.geometry.viewport) {
                        // Only geocodes have viewport.
                        bounds.union(place.geometry.viewport);
                    } else {
                        bounds.extend(place.geometry.location);
                    }
                });
                map.fitBounds(bounds);
            });
        }

        /**
         * Creates an instance of google.maps.LatLng by given lat and lng values and returns it.
         * This function can be useful for getting new coordinates quickly.
         * @param {!number} lat Latitude.
         * @param {!number} lng Longitude.
         * @return {google.maps.LatLng} An instance of google.maps.LatLng object
         */
        function getLocVar(lat, lng) {

            return new google.maps.LatLng(parseFloat(lat), parseFloat(lng));

        }

        function split(str){
            var res = str.split(",");

            return res;
        }

        // Adds a marker to the map.
        function addMarker(location, map) {
            // Add the marker at the clicked location, and add the next-available label
            // from the array of alphabetical characters.
            var marker = new google.maps.Marker({
                position: location,
                animation: google.maps.Animation.DROP,
                map: map,

            });
        }

    </script>

    <section class="main-container h-100">
        <div class="container-fluid h-100">
            <div class="row h-100">
                <input id="pac-input" class="controls" type="text" placeholder="Search Box">

                <!-- mapa -->
                <div class="col-12 h-500">
                    <div id="map"></div>
                </div>
                <!-- mapa -->


            </div>
        </div>
    </section>

@endsection
