@extends('custom_layout.dispecer.dispecer_app')

@section('content')

    @if (Session::has('message'))
        <div class="alert alert-info">{{ Session::get('message') }}</div>
    @endif

    <section class="main-container h-100">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-center">Všetky hlásenia</h1>

                </div>
                <div class="col-12 d-flex justify-content-end mb-3">
                    <form class="data-form" method="">
                        <select class="choose-data">
                            <option selected>Všetky hlásenia</option>
                            <option>Moje hlásenia</option>
                        </select>
                    </form>
                </div>
                <div class="col-12 d-flex justify-content-center flex-column">
                    <div class="table-responsive">
                        <table class="table main-table">
                            <thead>
                            <tr class="filter-row">
                                <th scope="col">#</th>
                                <th scope="col"><p>ID</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Poloha</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Zadané</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Kategória</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Stav problému</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Stav riešenia</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Priorita</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Používateľ</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Zamestnanec</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col"><p>Vozidlo</p><input type="text" class="input-filter w-100"></th>
                                <th scope="col">Edit</th>
                                <th scope="col">Detail</th>
                                <th scope="col">Vymaž záznam</th>

                            </tr>
                            </thead>
                            <tbody>

                            @php
                                $counter=1;
                            @endphp
                            @foreach($problems as $problem)

                                <tr>
                                    <td>{{ $counter }}</td>
                                    <td>{{ $problem->problem_id }}</td>
                                    <td>{{ $problem->poloha }}</td>
                                    <td>{{ $problem->created_at }}</td>
                                    <td>{{ $problem->KategoriaProblemu['nazov'] }}</td>
                                    <td>{{ $problem->StavProblemu['nazov'] }}</td>

                                    @foreach($typy_stavov_riesenia as $typ)
                                        @if($stavy_riesenia[$counter-1] == $typ->typ_stavu_riesenia_problemu_id)
                                            <td>{{ $typ->nazov }}</td>
                                        @endif
                                    @endforeach

                                    <td>{{ $problem->Priorita['priorita'] }}</td>
                                    <td>{{ $problem->users['name'] }}</td>

                                    @foreach($zamestnanci as $zamestnanec)
                                        @if($priradeni_zamestnanci[$counter-1] == 0)
                                            <td>Nepriradený</td>
                                            @break
                                        @else
                                            @if($priradeni_zamestnanci[$counter-1] == $zamestnanec->id)
                                                <td>{{ $zamestnanec->name }}</td>
                                            @endif
                                        @endif
                                    @endforeach



                                    @foreach($vozidla as $vozidlo)
                                        @if($priradene_vozidla[$counter-1] == 0)
                                            <td>Nepriradené</td>
                                            @break
                                        @else
                                            @if($priradene_vozidla[$counter-1] == $vozidlo->vozidlo_id)
                                                <td>{{ $vozidlo->SPZ }}</td>
                                            @endif
                                        @endif
                                    @endforeach


                                    <td><a href="/problem/{{ $problem->problem_id }}/edit" class="c-black"><i
                                                class="fas fa-edit"></i></a></td>
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

                                <div id="delete-modal-{{ $problem->problem_id }}" class="modal delete-modal"
                                     tabindex="-1"
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
                                                        <form
                                                            action="{{ route('problem.destroy', $problem->problem_id) }}"
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
                    </div>
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
