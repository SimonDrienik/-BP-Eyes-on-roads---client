<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class ZaregistrovanyObcanController extends Controller
{
    public function index()
    {
        //return view('zaregistrovany_obcan.home');
        return redirect('/problem');
    }

    public function __construct()
    {
        $this->middleware('auth');
        $this->middleware('role:zaregistrovany obcan');
    }
}
