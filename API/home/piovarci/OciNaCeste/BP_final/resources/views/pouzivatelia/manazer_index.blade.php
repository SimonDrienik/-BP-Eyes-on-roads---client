@extends('custom_layout.manazer.manazer_app')

@section('content')

    @if (Session::has('message'))
        <div class="alert alert-info">{{ Session::get('message') }}</div>
    @endif

    <section class="main-container h-100">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-center">Dispečeri</h1>
                </div>
                <div class="col-12 d-flex justify-content-center flex-column">
                    <table class="table main-table">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">ID</th>
                            <th scope="col">Meno</th>
                            <th scope="col">Email</th>
                            <th scope="col">Vytvorené</th>
                            <th scope="col">Priradené problémy</th>

                        </tr>
                        </thead>
                        <tbody>
                        @php
                            $counter = 1;
                        @endphp
                        @foreach($users as $user)

                            <tr>
                                <td>{{ $counter }}</td>
                                <td>{{ $user->id }}</td>
                                <td>{{ $user->name }}</td>
                                <td>{{ $user->email }}</td>

                                <td>{{ $user->created_at }}</td>

                                <td><a href="" class="c-black"><i class="fas fa-edit"></i></a></td>

                            </tr>
                            @php
                                $counter++;
                            @endphp
                        @endforeach

                        </tbody>

                    </table>


                    @if(!empty(Session::get('success')))
                        <div class="alert alert-success"> {{ Session::get('success') }}</div>
                    @endif
                    @if(!empty(Session::get('error')))
                        <div class="alert alert-danger"> {{ Session::get('error') }}</div>
                    @endif

                </div>
            </div>
        </div>

    </section>
@endsection
