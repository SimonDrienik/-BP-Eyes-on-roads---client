<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class FotkaStavuRieseniaProblemu extends Model
{
    protected $fillable = ['fotka_stavu_riesenia_problemu_id', 'cesta_k_suboru', 'popis_stavu_riesenia_id'];

    protected $table='fotka_stavu_riesenia_problemu';
    protected $primaryKey ='fotka_stavu_riesenia_problemu_id';

    public function PopisStavuRieseniaProblemu(){
        return $this->HasMany('App\Models\PopisStavuRieseniaProblemu', 'popis_stavu_riesenia_id');
    }


}
