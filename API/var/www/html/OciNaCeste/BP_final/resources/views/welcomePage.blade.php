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


            var JSARR = <?php echo json_encode($stavy_riesenia); ?>;
            var popisyArr = <?php echo json_encode($popisyArr); ?>;


            var poc = 0;
            var nazov_typu_riesenia;
            var popis_stavu_riesenia;

                @foreach($problems as $problem)

            var loc = split(" {{ $problem->poloha }}");


            @foreach($typy_stavov_riesenia as $typ)

            if (JSARR[poc] == "{{$typ->typ_stavu_riesenia_problemu_id}}") {
                nazov_typu_riesenia = "{{$typ->nazov}}"
            }

            @endforeach

                @foreach($popisyAll as $popis)

            if (popisyArr[poc] == 0) {

                popis_stavu_riesenia = "Nepriradený popis"

            } else if (popisyArr[poc] == "{{$popis->popis_stavu_riesenia_problemu_id}}") {

                popis_stavu_riesenia = "{{$popis->popis}}"


            }
            @endforeach

                poc++;
            addMarker(getLocVar(loc[0], loc[1]), map, "{{ $problem->created_at}}",
                "{{ $problem->poloha }}", "{{ $problem->popis_problemu }}",
                "{{ $problem->KategoriaProblemu['nazov'] }}",
                "{{ $problem->StavProblemu['nazov'] }}",
                nazov_typu_riesenia, "{{$problem->problem_id}}", popis_stavu_riesenia);

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

        function split(str) {
            var res = str.split(",");

            return res;
        }

        // Adds a marker to the map.
        function addMarker(location, map, created_at, poloha, popis, kategoria, stav, typ_stavu_riesenia, id, popisRiesenia) {
            // Add the marker at the clicked location, and add the next-available label
            // from the array of alphabetical characters.
            var marker = new google.maps.Marker({
                position: location,
                animation: google.maps.Animation.DROP,
                map: map,

            });


            if (popisRiesenia == null) {
                popisRiesenia = "Nepriradený"
            }

            var infowindow = new google.maps.InfoWindow({
                content: "<p>" + "<b>ID: </b>" + id + "</p>" +
                    "<p>" + "<b>Vytvorené dňa: </b>" + created_at + "</p>"
                    + "<p>" + "<b>Poloha: </b>" + poloha + "</p>"
                    + "<p>" + "<b>Popis: </b>" + popis + "</p>"
                    + "<p>" + "<b>Kategória: </b>" + kategoria + "</p>"
                    + "<p>" + "<b>Stav problému: </b>" + stav + "</p>"
                    + "<p>" + "<b>Stav riešenia problému: </b>" + typ_stavu_riesenia + "</p>"
                    + "<p>" + "<b>Popis stavu riešenia problému: </b>" + popisRiesenia + "</p>"
            });

            marker.addListener('click', function () {
                infowindow.open(map, marker);
            });
        }


    </script>

    <section class="main-container h-100">
        <div class="container-fluid h-100">
            <div class="row">
                <div class="col-12 mb-4 nadpis">
                    <h3>Ak si našiel problém na ceste alebo v jej okolí (stav vozovky, dopravné značenie, kvalita opravy
                        alebo zeleň), daj nám o ňom vedieť!</h3>

                </div>
                <input id="pac-input" class="controls" type="text" placeholder="Vyhľadať">

                <div class="col-12 h-500 ">
                    <div id="map"></div>
                    <p class="mt-1"> *Ľavým klikom na ľubovoľné označenie otvorí okno s detailnými informáciami.
                    </p>


                </div>

            </div>
        </div>
    </section>

@endsection
