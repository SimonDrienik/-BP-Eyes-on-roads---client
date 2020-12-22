<!doctype html>
<html lang="sk">
<html manifest="MANIFEST">

<head>
    @include('custom_layout.partials.head')
</head>

<body>
@include('custom_layout.partials.welcomePage.welcomePage_nav')


<main>
    @yield('content')
</main>


<!-- Bootstrap core JavaScript -->
@include('custom_layout.partials.footer-scripts')
</body>
</html>
