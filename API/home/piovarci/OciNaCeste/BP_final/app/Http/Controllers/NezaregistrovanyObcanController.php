<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class NezaregistrovanyObcanController extends Controller
{
    public function index()
    {
        return view('nezaregistrovany_obcan.home');
    }

    public function __construct()
    {
        $this->middleware('auth');
        $this->middleware('role:nezaregistrovany obcan');
    }
}
