<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Cesta extends Model
{
    protected $fillable = ['nazov','cesta_id', 'kraj_id', 'katastralne_uzemie_id', 'obec_id', 'spravca_id'];

    protected $table='cesta';
    protected $primaryKey ='cesta_id';

    public function Kraj(){
        return $this->belongsTo('App\Models\Kraj', 'kraj_id');
    }

    public function KatastralneUzemie(){
        return $this->belongsTo('App\Models\KatastralneUzemie', 'katastralne_uzemie_id');
    }

    public function Obec(){
        return $this->belongsTo('App\Models\Obec', 'obec_id');
    }

    public function Spravca(){
        return $this->belongsTo('App\Models\Spravca', 'spravca_id');
    }

}
