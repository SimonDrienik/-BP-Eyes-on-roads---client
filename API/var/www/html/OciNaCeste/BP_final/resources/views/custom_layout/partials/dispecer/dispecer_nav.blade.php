@if($errors->all() && Request::path() == 'problem')
    <script>
        $(function() {
            $('#edit-modal-{{Auth::user()->id}}').modal('show');
        });
    </script>
@endif

<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="d-flex align-items-center wrap-mobile">
            <a class="title-link" href="/problem"><h1>Oči na ceste</h1></a>
            <img src="{{ asset('img/fiitLogo.png') }}" height="50" class="ml-3">
            <img src="{{ asset('img/sucttskLogo2.png') }}" height="50" class="ml-3">
        </div>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ml-auto align-items-center">
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
                    <a class="nav-link" href="{{ route('login') }}">{{ __('Prihlásenie') }}</a>
                </li>
                @if (Route::has('register'))
                    <li class="nav-item">
                        <a class="nav-link" href="{{ route('register') }}">{{ __('Registrácia') }}</a>
                    </li>
                @endif
            @else
                <li class="nav-item dropdown">
                    <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" v-pre>
                        {{ Auth::user()->name }} <span class="caret"></span>
                    </a>

                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">

                        <button class="dropdown-item" data-toggle="modal"
                                data-target="#edit-modal-{{Auth::user()->id}}">Nastavenia
                        </button>


                        <a class="dropdown-item" href="{{ route('logout') }}"
                           onclick="event.preventDefault();
                                                     document.getElementById('logout-form').submit();">
                            {{ __('Odhlásenie') }}
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


<div id="edit-modal-{{ Auth::user()->id }}" class="modal edit-modal" tabindex="-1"
     role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Zmena používateľského konta</h5>
            </div>

            @if ($errors->any())
                <div class="alert alert-danger">
                    <strong>Whoops!</strong> There were some problems with your input.<br><br>
                    <ul>
                        @foreach ($errors->all() as $error)
                            <li>{{ $error }}</li>
                        @endforeach
                    </ul>
                </div>
            @endif
            <div class="modal-body text-center">
                <form action="{{ route('pouzivatelia.update', Auth::user()->id) }}"
                      method="POST" class="w-100">
                    @csrf
                    @method('PUT')
                    <div>
                        <label class="update-label">Meno a Priezvisko:</label>
                        <input id="name" class="form-input m-auto" type="text" name="name"
                               value="{{Auth::user()->name}}">
                    </div>
                    <div>
                        <label class="update-label">Email:</label>
                        <input id="email" class="form-input m-auto" type="text" name="email"
                               value="{{Auth::user()->email}}">
                    </div>
                    <div>
                        <label class="update-label">Nové heslo:</label>
                        <input id="password" type="password"
                               class="form-input m-auto"
                               name="password"
                        >
                    </div>
                    <div>
                        <label class="update-label">Potvrďte nové heslo:</label>
                        <input id="password-confirm" type="password" class="form-input m-auto"
                               name="password_confirmation">

                    </div>
                    <ul class="d-flex align-items-center justify-content-center mt-4">
                        <li>
                            <button type="button" class="btn btn-primary cancel mr-4"
                                    data-dismiss="modal"
                                    aria-label="Close">Zrušiť
                            </button>
                        </li>
                        <li>
                            <button type="submit"
                                    class="btn btn-primary update-btn mr-3">Aktualizovať
                            </button>
                        </li>
                    </ul>
                </form>
            </div>
        </div>
    </div>
</div>
