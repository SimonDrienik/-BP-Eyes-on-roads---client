<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Kraj extends Model
{
    protected $fillable = ['kraj_id', 'nazov'];

    protected $table='kraj';
    protected $primaryKey ='kraj_id';
}
