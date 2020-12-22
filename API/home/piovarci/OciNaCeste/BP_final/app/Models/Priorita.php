<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Priorita extends Model
{
    protected $fillable = ['priorita_id', 'priorita'];

    protected $table='priorita';
    protected $primaryKey ='priorita_id';
}
