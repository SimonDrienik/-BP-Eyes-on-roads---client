<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class KategoriaProblemu extends Model
{
    protected $fillable = ['kategoria_problemu_id', 'nazov'];

    protected $table='kategoria_problemu';
    protected $primaryKey ='kategoria_problemu_id';
}
