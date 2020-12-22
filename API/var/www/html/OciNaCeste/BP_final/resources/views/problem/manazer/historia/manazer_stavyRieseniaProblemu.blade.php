@extends('custom_layout.manazer.manazer_app')

@section('content')

    @if (Session::has('message'))
        <div class="alert alert-info">{{ Session::get('message') }}</div>
    @endif

    <div id="delete-modal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Vymazať záznam</h5>
                </div>
                <div class="modal-body">

                    <p>Ste si istý, že chcete vymazať záznam?</p>
                    <ul class="d-flex align-items-center justify-content-center mt-4">
                        <li>
                            <button type="button" class="btn btn-primary cancel mr-4" data-dismiss="modal"
                                    aria-label="Close">Zrušiť
                            </button>
                        </li>
                        <!--<li><button type="button" class="btn btn-danger delete">Vymazať</button></li>-->
                    </ul>
                </div>
            </div>
        </div>
    </div>



    <section class="main-container h-100">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-center">    História priradených stavov riešenia problému pre problém
                        ID {{$problem->problem_id}}</h1>
                </div>


                <div class="col-12 d-flex justify-content-center flex-column">
                    <div class="table-responsive">
                        <table class="table main-table">
                            <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">ID</th>
                                <th scope="col">Názov Stavu</th>
                                <th scope="col">Pridelené</th>

                            </tr>
                            </thead>
                            <tbody>
                            @php
                                $counter = 1;
                            @endphp
                            @foreach($stavy as $stav)

                                <tr>
                                    <td>{{ $counter }}</td>
                                    <td>{{ $stav->stav_riesenia_problemu_id }}</td>
                                    <td>{{ $stav->TypStavuRieseniaProblemu['nazov'] }}</td>
                                    <td>{{ $stav->created_at }}</td>

                                </tr>
                                @php
                                    $counter++;
                                @endphp

                            @endforeach

                            </tbody>

                        </table>
                    </div>


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
