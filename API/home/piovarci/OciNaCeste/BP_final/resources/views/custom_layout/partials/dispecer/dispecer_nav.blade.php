<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="d-flex align-items-center">
            <img src="{{ asset('img/fiitLogo.png') }}" height="50">
            <img src="{{ asset('img/sucttskLogo2.png') }}" height="50" class="ml-3">
        </div>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav m-auto">
                <li class="nav-item {{ Request::is('problem/create') ? 'active' : '' }}">
                    <a class="nav-link" href="/problem/create">Vytvor hlásenie</a>
                </li>
                <li class="nav-item {{ Request::is('problem') ? 'active' : '' }}">
                    <a class="nav-link" href="/problem">Všetky hlásenia</a>
                </li>
                <li class="nav-item {{ Request::is('mapa') ? 'active' : '' }}">
                    <a class="nav-link" href="/mapa">Mapa</a>
                </li>
            </ul>
        </div>

        <!-- Right Side Of Navbar -->
        <ul class="navbar-nav ml-auto">
            <!-- Authentication Links -->
            @guest
                <li class="nav-item">
                    <a class="nav-link" href="{{ route('login') }}">{{ __('Login') }}</a>
                </li>
                @if (Route::has('register'))
                    <li class="nav-item">
                        <a class="nav-link" href="{{ route('register') }}">{{ __('Register') }}</a>
                    </li>
                @endif
            @else
                <li class="nav-item dropdown">
                    <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                        {{ Auth::user()->name }} <span class="caret"></span>
                    </a>

                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="{{ route('logout') }}"
                           onclick="event.preventDefault();
                                                     document.getElementById('logout-form').submit();">
                            {{ __('Logout') }}
                        </a>

                        <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
                            @csrf
                        </form>
                    </div>
                </li>
            @endguest
        </ul>
        <button class="navbar-toggler ml-3" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
    </nav>
</header>
