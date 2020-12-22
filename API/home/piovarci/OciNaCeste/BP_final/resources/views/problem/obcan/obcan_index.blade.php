@extends('custom_layout.obcan.obcan_app')

@section('content')

    @if (Session::has('message'))
        <div class="alert alert-info">{{ Session::get('message') }}</div>
    @endif


    <section class="main-container h-100">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-center">Moje hlásenia</h1>
                </div>
                <div class="col-12 d-flex justify-content-center flex-column">
                    <table class="table main-table">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Poloha</th>
                            <th scope="col">Vytvorené dňa</th>
                            <th scope="col">Kategória problému</th>
                            <th scope="col">Stav problému</th>
                            <th scope="col">Stav riešenia</th>
                            <th scope="col">Detail</th>
                            <th scope="col">Vymaž záznam</th>

                        </tr>
                        </thead>
                        <tbody>
                        @php
                            $counter = 1;
                        @endphp
                        @foreach($problems as $problem)

                            <tr>
                                <td>{{ $counter }}</td>
                                <td>{{ $problem->poloha }}</td>
                                <td>{{ $problem->created_at }}</td>
                                <td>{{ $problem->KategoriaProblemu['nazov'] }}</td>
                                <td>{{ $problem->StavProblemu['nazov'] }}</td>
                                @foreach($typy_stavov_riesenia as $typ)
                                    @if($stavy_riesenia[$counter-1] == $typ->typ_stavu_riesenia_problemu_id)
                                        <td>{{ $typ->nazov }}</td>
                                    @endif
                                @endforeach

                                <td><a href="/problem/{{ $problem->problem_id }}" class="c-black"><i
                                            class="fas fa-info"></i></a></td>
                                <td>
                                    <p class="text-center">
                                        <button type="submit" class="border-0" data-toggle="modal"
                                                data-target="#delete-modal-{{ $problem->problem_id }}"><i
                                                class="far fa-trash-alt"></i>
                                        </button>
                                    </p>
                                </td>
                            </tr>
                            @php
                                $counter++;
                            @endphp


                            <div id="delete-modal-{{ $problem->problem_id }}" class="modal delete-modal" tabindex="-1"
                                 role="dialog">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Vymazať záznam</h5>
                                        </div>
                                        <div class="modal-body">

                                            <p>Ste si istý, že chcete vymazať záznam?</p>
                                            <ul class="d-flex align-items-center justify-content-center mt-4">
                                                <li>
                                                    <button type="button" class="btn btn-primary cancel mr-4"
                                                            data-dismiss="modal"
                                                            aria-label="Close">Zrušiť
                                                    </button>
                                                </li>
                                                <li>
                                                    <form action="{{ route('problem.destroy', $problem->problem_id) }}"
                                                          method="POST">
                                                        @method('DELETE')
                                                        @csrf
                                                        <p class="text-center">
                                                            <button type="submit" class="btn btn-danger delete">
                                                                Vymazať
                                                            </button>
                                                        </p>

                                                    </form>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>



                        @endforeach


                        </tbody>
                    </table>

                    {{$problems->links()}}

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
