<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', 'ProblemController@welcomePage')->name('welcome');
	//PRIDAL SIMON DRIENIK, VOLANIE FUNKCIE NA ZOBRAZENIE MARKEROV NA MAPE V ANDROID APPKE
	//pridal simon drienik, volanie finkcie na pridanie markeru neregistrovanym uzivatelom

Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');
Route::get('/zaregistrovanyObcan',
    'ZaregistrovanyObcanController@index')->name('zaregistrovanyObcan');
Route::get('/nezaregistrovanyObcan',
    'NezaregistrovanyObcanController@index')->name('nezaregistrovanyObcan');
Route::get('/admin', 'AdminController@index')->name('admin');
Route::get('/dispecer', 'DispecerController@index')->name('dispecer');
Route::get('/manazer', 'ManazerController@index')->name('manazer');

Route::resource('problem', 'ProblemController');
Route::resource('pouzivatelia', 'UserController');
Route::resource('cesta', 'CestaController');
Route::post('filtrovaneProblemy', 'ProblemController@filter')->name('filtered');
Route::post('/moje', 'ProblemController@priradeneProblemyDispecerovi')->name('problem.priradeneDispecerovi');
Route::get('/mapa', 'ProblemController@mapa')->name('mapa');

Route::get('/problem/priradeneDispecerovi',
    'ProblemController@priradeneProblemyDispecerovi')->name('priradeneDispecerovi');

Route::get('/problem/{problem}/priradeniZamestnanci',
    'ProblemController@priradeniZamestnanci')->name('priradeniZamestnanci');
Route::get('/problem/{problem}/priradeneVozidla',
    'ProblemController@priradeneVozidla')->name('priradeneVozidla');
Route::get('/problem/{problem}/stavyRieseniaProblemu',
    'ProblemController@stavyRieseniaProblemu')->name('stavyRieseniaProblemu');
Route::get('/problem/{problem}/popisyStavovRieseniaProblemu',
    'ProblemController@popisyStavovRieseniaProblemu')->name('popisyStavovRieseniaProblemu');

Route::get('/welcomePage/create', 'ProblemController@welcomePageCreate')->name('welcomePage.create');
Route::post('/welcomePage', 'ProblemController@welcomePageStore')->name('welcomePage.store');
;
Route::get('/showAllAndroid', 'ProblemController@showAllProblemsAndroid')->name('showAllAndroid');
Route::get('/unregisteredPostAndroid/{poloha}/{popis_problemu}/{kategoria_problemu}/{stav_problemu}', 'ProblemController@unregisteredAddRecordAndroid')->name('unregisteredPostAndroid');
