<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Vozidlo extends Model
{
    protected $fillable = ['vozidlo_id', 'oznacenie', 'SPZ', 'pocet_najazdenych_km', 'poznamka'];

    protected $table='vozidlo';
    protected $primaryKey ='vozidlo_id';
}
