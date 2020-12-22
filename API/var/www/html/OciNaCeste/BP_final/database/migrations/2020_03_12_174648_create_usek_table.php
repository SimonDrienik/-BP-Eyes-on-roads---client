<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateUsekTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('usek', function (Blueprint $table) {
            $table->bigIncrements('usek_id');
            $table->text('zaciatocny_bod');
            $table->text('koncovy_bod');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('usek', function (Blueprint $table) {

            $table->bigInteger('cesta_id')->unsigned();


            $table->foreign('cesta_id')
                ->references('cesta_id')->on('cesta')
                ->onDelete('cascade');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('usek');
    }
}
