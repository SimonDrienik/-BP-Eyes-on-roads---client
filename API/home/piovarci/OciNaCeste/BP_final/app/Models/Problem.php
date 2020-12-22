<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Problem extends Model
{
    protected $fillable = ['problem_id', 'poloha', 'popis_problemu', 'priorita_id', 'cesta_id', 'pouzivatel_id',
        'kategoria_problemu_id', 'stav_problemu_id',
        ];

    protected $table = 'problem';
    protected $primaryKey = 'problem_id';

    protected $attributes = [
        'priorita_id' => 1, //nepriradena
        'cesta_id' => 1, // default
    ];

    //A Problem can only have one Priorita
    public function Priorita()
    {
        return $this->belongsTo('App\Models\Priorita', 'priorita_id');
    }

    public function Cesta()
    {
        return $this->belongsTo('App\Models\Cesta', 'cesta_id');
    }

    public function users()
    {
        return $this->belongsTo('App\User', 'pouzivatel_id');
    }

    public function KategoriaProblemu()
    {
        return $this->belongsTo('App\Models\KategoriaProblemu', 'kategoria_problemu_id');
    }


    public function StavProblemu()
    {
        return $this->belongsTo('App\Models\StavProblemu', 'stav_problemu_id');
    }

}
