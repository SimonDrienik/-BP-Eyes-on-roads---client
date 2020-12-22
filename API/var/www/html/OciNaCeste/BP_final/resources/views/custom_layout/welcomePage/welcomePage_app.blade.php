<!doctype html>
<html lang="sk">
<html manifest="MANIFEST">

<head>
    @include('custom_layout.partials.head')
</head>

<body class="{{ Request::is('/') ? 'footer-top' : '' }}">
@include('custom_layout.partials.welcomePage.welcomePage_nav')


<main>
    @yield('content')
</main>


<!-- Bootstrap core JavaScript -->
@include('custom_layout.partials.footer-scripts')

<script>
    $(document).ready(function() {
        var mainHeight = $('.footer-top main').height() - $('.footer-top footer').height();
        $('.footer-top main').height(mainHeight).css('min-height', '100%');
    })
</script>
</body>
</html>
